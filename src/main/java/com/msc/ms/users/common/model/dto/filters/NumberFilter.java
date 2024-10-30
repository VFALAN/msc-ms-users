package com.msc.ms.users.common.model.dto.filters;

import com.msc.ms.users.common.cosntans.FilterType;
import com.msc.ms.users.common.cosntans.ValueType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NumberFilter extends FilterCriteria{
    private final Integer value;
    private final Integer minValue;
    private final Integer maxValue;

    @Builder
    public NumberFilter(FilterType pFilterType,
                        ValueType pValueType,
                        Integer pValueInt,
                        Integer pMinValue,
                        Integer pMaxValue){
        super(pFilterType, pValueType);
        value = pValueInt;
        minValue = pMinValue;
        maxValue = pMaxValue;
    }
}
