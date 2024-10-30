package com.msc.ms.users.user.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.msc.ms.users.common.cosntans.SortType;
import com.msc.ms.users.common.cosntans.ValueType;
import com.msc.ms.users.common.model.dto.ColumnData;
import com.msc.ms.users.common.model.dto.DataTableRequest;
import com.msc.ms.users.common.model.dto.DynamicDataTableResponse;
import com.msc.ms.users.common.model.dto.filters.*;
import com.msc.ms.users.common.utils.FieldsUtils;
import com.msc.ms.users.user.model.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserDatatableService {

    private final EntityManager entityManager;
    private final Class searchClass;
    private final ObjectMapper objectMapper;

    public UserDatatableService(EntityManager pEntityManager, ObjectMapper pObjectMapper) {
        entityManager = pEntityManager;
        objectMapper = pObjectMapper;
        searchClass = UserEntity.class;
    }


    public DynamicDataTableResponse<Object> userPageByCriteria(
            DataTableRequest pDataTableRequest) {
        final var mTotalRecordsLong = countRecords(pDataTableRequest.getColumns());
        ArrayNode mJsonArrayData = objectMapper.createArrayNode();
        var mTotalPagesInt = 0;
        if (mTotalRecordsLong > 0) {
            mJsonArrayData = this.searchData(pDataTableRequest.getColumns(), pDataTableRequest.getPage(), pDataTableRequest.getSize());
            mTotalPagesInt = (int) (mTotalRecordsLong / pDataTableRequest.getSize());
        }
        return DynamicDataTableResponse.builder()
                .totalPages(mTotalPagesInt)
                .data(mJsonArrayData)
                .totalRecords(mTotalRecordsLong)
                .currentPage(pDataTableRequest.getPage())
                .build();

    }

    private Long countRecords(List<ColumnData> pColumnDataList) {
        final var mCriteriaBuilder = entityManager.getCriteriaBuilder();
        final var mCriteriaQueryLong = mCriteriaBuilder.createQuery(Long.class);
        final var mRootUserEntity = mCriteriaQueryLong.from(UserEntity.class);
        final var mPredicateList = buildPredicates(mRootUserEntity, mCriteriaBuilder, pColumnDataList);
        mCriteriaQueryLong.select(mCriteriaBuilder.count(mRootUserEntity)).where(mPredicateList.toArray(new Predicate[0]));
        return entityManager.createQuery(mCriteriaQueryLong).getSingleResult();


    }


    private ArrayNode searchData(List<ColumnData> data, int page, int size) {
        final var mCriteriaBuilder = entityManager.getCriteriaBuilder();
        final var mTupleQuery = mCriteriaBuilder.createTupleQuery();
        final var mRoot = mTupleQuery.from(UserEntity.class);
        final var firstRow = page > 0 ? page * size : 0;
        final var mMultipleSelectTupleQuery = buildMainQuery(mRoot, mCriteriaBuilder, data, mTupleQuery);
        final var mPredicatesList = buildPredicates(mRoot, mCriteriaBuilder, data);
        final var mOrderList = buildOrders(mRoot, mCriteriaBuilder, data);
        mMultipleSelectTupleQuery.where(mPredicatesList.toArray(new Predicate[0])).orderBy(mOrderList);
        ArrayNode mJsonNodeArray = objectMapper.createArrayNode();
        final var mSdf = new SimpleDateFormat("dd-MM-yyyy");
        entityManager.createQuery(mMultipleSelectTupleQuery).setFirstResult(firstRow).setMaxResults(size).getResultList().forEach(tuple ->
        {
            final var mTempJsonNode = objectMapper.createObjectNode();
            data.forEach(columnData -> {
                switch (columnData.getValueType()) {
                    case NUMBER ->
                            mTempJsonNode.put(columnData.getAlias(), tuple.get(columnData.getAlias(), Integer.class));
                    case STRING ->
                            mTempJsonNode.put(columnData.getAlias(), tuple.get(columnData.getAlias(), String.class));
                    case DATE ->
                            mTempJsonNode.put(columnData.getAlias(), mSdf.format(tuple.get(columnData.getAlias(), Date.class)));
                    case BOOLEAN ->
                            mTempJsonNode.put(columnData.getAlias(), tuple.get(columnData.getAlias(), Boolean.class));
                    default -> {
                        log.warn("No type for tuple");
                        log.debug("type searched: {} for alias: {} path property: {}", columnData.getValueType(), columnData.getAlias(), columnData.getPropertyPath());
                    }
                }

            });
            mJsonNodeArray.add(mTempJsonNode);

        });
        return mJsonNodeArray;
    }


    private Class getClass(ValueType valueType) {
        return switch (valueType) {
            case NUMBER -> Integer.class;
            case STRING -> String.class;
            case BOOLEAN -> Boolean.class;
            case DATE -> Date.class;
            default -> {
                log.error("no Value Type for mapping tuple function");
                yield null;
            }
        };
    }

    private void mapData() {
    }

    private CriteriaQuery<Tuple> buildMainQuery(Root<UserEntity> pRootUserEntity, CriteriaBuilder pCriteriaBuilder, List<ColumnData> mColumnData, CriteriaQuery<Tuple> mQuery) {
        final List<Selection<?>> mSelectionList = new ArrayList<>();
        mColumnData.forEach(columnData -> {
            final Path<?> path = getPropertyPath(pRootUserEntity, columnData.getPropertyPath());
            mSelectionList.add(path.alias(columnData.getAlias()));
        });
        return mQuery.multiselect(mSelectionList);
    }

    private List<Predicate> buildPredicates(Root<UserEntity> pRootUserEntity, CriteriaBuilder pCriteriaBuilder, List<ColumnData> pColumnDataRequest) {
        final var mPredicateList = new ArrayList<Predicate>();
        pColumnDataRequest.forEach(columnData -> {
            if (columnData.getIsFiltered()) {
                final var mPredicate = buildPredicate(pRootUserEntity, pCriteriaBuilder, columnData);
                if (mPredicate != null) {
                    mPredicateList.add(mPredicate);
                }
            }
        });
        return mPredicateList;
    }

    private Predicate buildPredicate(Root<UserEntity> pRootUserEntity,
                                     CriteriaBuilder pCriteriaBuilder,
                                     ColumnData pColumData) {
        if (FieldsUtils.validIfClassHasField(searchClass, pColumData.getPropertyPath())) {
            return switch (pColumData.getFilter().getValueType()) {
                case DATE -> createDateFilter(pRootUserEntity, pCriteriaBuilder, pColumData);

                case NUMBER -> createNumberFilter(pRootUserEntity, pCriteriaBuilder, pColumData);

                case STRING -> createStringFilter(pRootUserEntity, pCriteriaBuilder, pColumData);

                case BOOLEAN -> createBooleanFilter(pRootUserEntity, pCriteriaBuilder, pColumData);

                default -> {
                    log.warn("No Search Type: {} found For: {}", pColumData.getFilter().getValueType(), pColumData.getAlias());
                    yield null;
                }

            };
        }
        return null;

    }

    private Predicate createStringFilter(Root<UserEntity> pRootUserEntity, CriteriaBuilder pCriteriaBuilder, ColumnData pColumData) {
        final var mStringFilter = (StringFilter) pColumData.getFilter();
        final Path<String> mPath = (Path<String>) getPropertyPath(pRootUserEntity, pColumData.getPropertyPath());
        return switch (mStringFilter.getFilterType()) {
            case EQUALS -> pCriteriaBuilder.equal(mPath, mStringFilter.getValue());
            case LIKE -> pCriteriaBuilder.like(mPath, "%" + mStringFilter.getValue() + "%");
            case END_WITH -> pCriteriaBuilder.like(mPath, "%" + mStringFilter.getValue());
            case START_WITH -> pCriteriaBuilder.like(mPath, mStringFilter.getValue() + "%");
            case NOT_END -> pCriteriaBuilder.notLike(mPath, "%" + mStringFilter.getValue());
            case NOT_START -> pCriteriaBuilder.notLike(mPath, mStringFilter.getValue() + "%");
            case DIFFERENT -> pCriteriaBuilder.notEqual(mPath, mStringFilter.getValue());
            default -> {
                log.error("no search type for String filter: {} in field: {}", mStringFilter.getFilterType(), pColumData.getAlias());
                yield null;
            }
        };
    }

    private Predicate createBooleanFilter(Root<UserEntity> pRootUserEntity,
                                          CriteriaBuilder pCriteriaBuilder, ColumnData pColumnData) {
        //final FilterCriteria<Boolean>
        final var mBooleanFilter = (BooleanFilter) pColumnData.getFilter();
        final Path<Boolean> mPath = (Path<Boolean>) getPropertyPath(pRootUserEntity, pColumnData.getPropertyPath());
        return switch (mBooleanFilter.getFilterType()) {
            case IS_TRUE -> pCriteriaBuilder.isTrue(mPath);
            case IS_FALSE -> pCriteriaBuilder.isFalse(mPath);
            default -> {
                log.error("no search type for Boolean filter: {} in field: {}", mBooleanFilter.getFilterType(), pColumnData.getAlias());
                yield null;
            }
        };

    }

    private Predicate createNumberFilter(Root<UserEntity> pRootUserEntity,
                                         CriteriaBuilder pCriteriaBuilder, ColumnData pColumnData) {

        final var mNumberFilter = (NumberFilter) pColumnData.getFilter();
        final Path<Integer> mPath = (Path<Integer>) getPropertyPath(pRootUserEntity, pColumnData.getPropertyPath());
        return switch (mNumberFilter.getFilterType()) {
            case EQUALS -> pCriteriaBuilder.equal(mPath, mNumberFilter.getValue());
            case DIFFERENT -> pCriteriaBuilder.notEqual(mPath, mNumberFilter.getValue());
            case LESS_THAN -> pCriteriaBuilder.lessThan(mPath, mNumberFilter.getValue());
            case BIGGER_THAN -> pCriteriaBuilder.greaterThan(mPath, mNumberFilter.getValue());
            case LESS_OR_EQUALS -> pCriteriaBuilder.lessThanOrEqualTo(mPath, mNumberFilter.getValue());
            case BIGGER_OR_EQUALS -> pCriteriaBuilder.greaterThanOrEqualTo(mPath, mNumberFilter.getValue());
            default -> {
                log.error("Filter type not compatible with Number Filter field:{} , filterType: {}", pColumnData.getAlias(), pColumnData.getPropertyPath());
                yield null;
            }
        };

    }

    private Predicate createDateFilter(Root<UserEntity> pRootUserEntity,
                                       CriteriaBuilder pCriteriaBuilder, ColumnData pColumnData) {
        Predicate predicate = null;
        final DateFilter mFilterDate = (DateFilter) pColumnData.getFilter();
        final Path<Date> mPath = (Path<Date>) getPropertyPath(pRootUserEntity, pColumnData.getPropertyPath());
        switch (mFilterDate.getFilterType()) {
            case EQUALS -> predicate = pCriteriaBuilder.equal(mPath, mFilterDate.getValue());
            case BETWEEN ->
                    predicate = pCriteriaBuilder.between(mPath, mFilterDate.getStartDate(), mFilterDate.getEndDate());
            case AFTER -> predicate = pCriteriaBuilder.greaterThan(mPath, mFilterDate.getValue());
            case BEFORE -> predicate = pCriteriaBuilder.lessThan(mPath, mFilterDate.getValue());
            case DIFFERENT -> predicate = pCriteriaBuilder.notEqual(mPath, mFilterDate.getValue());
            default -> log.warn("No Filter Type Found for Date Object in field: {}", pColumnData.getAlias());
        }
        return predicate;
    }

    private List<Order> buildOrders(Root<UserEntity> pRootUserEntity, CriteriaBuilder pBuilder, List<ColumnData> mColumnDataList) {
        final var mOrderList = new ArrayList<Order>();
        mColumnDataList.forEach(columnData -> {
            if (columnData.getIsSorted()) {
                final var path = this.getPropertyPath(pRootUserEntity, columnData.getPropertyPath());
                if (SortType.ASC == columnData.getSortType()) {
                    mOrderList.add(pBuilder.asc(path));
                } else {
                    mOrderList.add(pBuilder.desc(path));
                }
            }
        });
        return mOrderList;
    }

    private Path<?> getPropertyPath(Root<UserEntity> pRootUserEntity, String pPathStr) {
        final var levels = pPathStr.split("\\.");
        Path<?> tempPath = pRootUserEntity;
        for (final var level : levels) {
            tempPath = tempPath.get(level);
        }
        return tempPath;
    }


}
