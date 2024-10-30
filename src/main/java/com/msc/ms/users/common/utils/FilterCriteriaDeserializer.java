package com.msc.ms.users.common.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.msc.ms.users.common.cosntans.FilterType;
import com.msc.ms.users.common.cosntans.ValueType;
import com.msc.ms.users.common.model.dto.filters.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FilterCriteriaDeserializer extends StdDeserializer<FilterCriteria> {
    private static final String FILTER_TYPE = "filterType";
    private static final String VALUE_TYPE = "valueType";
    private static final String VALUE = "value";
    private static final String MIN_VALUE = "minValue";
    private static final String MAX_VALUE = "maxValue";
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public FilterCriteriaDeserializer() {
        super(FilterCriteria.class);
    }

    @Override
    public FilterCriteria deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        final var mObjectMapper = (ObjectMapper) jsonParser.getCodec();
        final JsonNode mNodeTree = mObjectMapper.readTree(jsonParser);
        final var mJsonNodeValueType = mNodeTree.get(VALUE_TYPE);
        final var mValueTypeEnum = ValueType.valueOf(mJsonNodeValueType.asText());
        final var mMapCommonProperties = this.getCommonValues(mJsonNodeValueType);
        if (hasProperties(mMapCommonProperties)) {
            return switch (mValueTypeEnum) {
                case BOOLEAN -> parseToBooleanFilter(mNodeTree);
                case STRING -> parseToStringFilter(mNodeTree);
                case DATE -> parseToDateFilter(mNodeTree);
                case NUMBER -> parseToNumberFilter(mNodeTree);
                default ->
                        throw new IllegalArgumentException(String.format("Invalid Value Type:  %s", mJsonNodeValueType.asText()));
            };
        } else {
            log.error("Error in object");
            mMapCommonProperties.forEach((key, value) -> log.error("{}:{}", key, value));
            throw new IllegalArgumentException("No deserializable Object");
        }


    }

    private BooleanFilter parseToBooleanFilter(JsonNode pJsonNode) {
        final var mValueTypeEnum = pJsonNode.get(VALUE_TYPE) != null ? ValueType.valueOf(pJsonNode.get(VALUE_TYPE).asText()) : null;
        final var mFilterTypeEnum = pJsonNode.get(FILTER_TYPE) != null ? FilterType.valueOf(pJsonNode.get(FILTER_TYPE).asText()) : null;
        final var mValueBln = pJsonNode.get(VALUE) != null ? pJsonNode.get(VALUE).asBoolean() : null;
        return BooleanFilter.builder()
                .pValueBln(mValueBln)
                .pFilterType(mFilterTypeEnum)
                .pValueType(mValueTypeEnum)
                .build();
    }

    private StringFilter parseToStringFilter(JsonNode pJsonNode) {
        final var mValueTypeEnum = pJsonNode.get(VALUE_TYPE) != null ? ValueType.valueOf(pJsonNode.get(VALUE_TYPE).asText()) : null;
        final var mFilterTypeEnum = pJsonNode.get(FILTER_TYPE) != null ? FilterType.valueOf(pJsonNode.get(FILTER_TYPE).asText()) : null;
        final var mValueStr = pJsonNode.get(VALUE) != null ? pJsonNode.get(VALUE).asText() : null;
        final var mRegexStr = pJsonNode.get("regex") != null ? pJsonNode.get("regex").asText() : null;
        return StringFilter.builder()
                .pValueStr(mValueStr)
                .pRegexStr(mRegexStr)
                .pFilterType(mFilterTypeEnum)
                .pValueType(mValueTypeEnum)
                .build();
    }

    private DateFilter parseToDateFilter(JsonNode pJsonNode) {
        try {
            final var mStartDate = pJsonNode.get("startDate") != null ? SIMPLE_DATE_FORMAT.parse(pJsonNode.get("startDate").asText()) : null;
            final var mEndDate = pJsonNode.get("endDate") != null ? SIMPLE_DATE_FORMAT.parse(pJsonNode.get("endDate").asText()) : null;
            final var mValueDate = pJsonNode.get(VALUE) != null ? SIMPLE_DATE_FORMAT.parse(pJsonNode.get(VALUE).asText()) : null;
            final var mFilterTypeEnum = pJsonNode.get(FILTER_TYPE) != null ? FilterType.valueOf(pJsonNode.get(FILTER_TYPE).asText()) : null;
            final var mValueTypeEnum = pJsonNode.get(VALUE_TYPE) != null ? ValueType.valueOf(pJsonNode.get(VALUE_TYPE).asText()) : null;
            return DateFilter.builder()
                    .pValueDate(mValueDate)
                    .pEndDate(mEndDate)
                    .pStartDate(mStartDate)
                    .pFilterType(mFilterTypeEnum)
                    .pValueType(mValueTypeEnum)
                    .build();
        } catch (ParseException e) {
            log.error("Parse exception in a DateFilter");
            log.debug("Object Structure:  \n {}", pJsonNode.asText());
            return null;
        }
    }

    private NumberFilter parseToNumberFilter(JsonNode pJsonNode) {
        final var mValueInt = Integer.valueOf(pJsonNode.get(VALUE).asText());
        final var mMinValueInt = pJsonNode.get(MIN_VALUE) != null ? Integer.valueOf(pJsonNode.get(MIN_VALUE).asText()) : null;
        final var mMaxValueInt = pJsonNode.get(MAX_VALUE) != null ? Integer.valueOf(pJsonNode.get(MAX_VALUE).asText()) : null;
        return NumberFilter.builder()
                .pFilterType(FilterType.valueOf(pJsonNode.get(VALUE_TYPE).asText()))
                .pValueType(ValueType.valueOf(pJsonNode.get(VALUE_TYPE).asText()))
                .pValueInt(mValueInt)
                .pMaxValue(mMaxValueInt)
                .pMinValue(mMinValueInt)
                .build();

    }

    private boolean hasProperties(Map<String, String> pPropertiesMap) {
        var hasAll = true;
        for (final var item : pPropertiesMap.entrySet()) {
            if (item.getValue() == null) {
                hasAll = false;
                break;
            }

        }
        return hasAll;
    }

    private Map<String, String> getCommonValues(JsonNode pJsonNode) {
        final var mFilterTypeStr = pJsonNode.get(FILTER_TYPE) != null ? pJsonNode.get(FILTER_TYPE).asText() : null;
        final var mValueTypeStr = pJsonNode.get(VALUE_TYPE) != null ? pJsonNode.get(VALUE_TYPE).asText() : null;
        final var mPropertiesMap = new HashMap<String, String>();
        mPropertiesMap.put(FILTER_TYPE, mFilterTypeStr);
        mPropertiesMap.put(VALUE_TYPE, mValueTypeStr);
        return mPropertiesMap;
    }

}
