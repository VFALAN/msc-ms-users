package com.msc.ms.users.common.model.dto.filters;

import com.msc.ms.users.common.cosntans.FilterType;
import com.msc.ms.users.common.cosntans.ValueType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DateFilter extends FilterCriteria {

    private final Date value;
    private final Date startDate;
    private final Date endDate;

    @Builder
    public DateFilter(FilterType pFilterType,
                      ValueType pValueType,
                      Date pValueDate,
                      Date pStartDate,
                      Date pEndDate) {
        super(pFilterType, pValueType);
        value = pValueDate;
        startDate = pStartDate;
        endDate = pEndDate;
    }
}

