package com.msc.ms.users.common.model.dto.filters;

import com.msc.ms.users.common.cosntans.FilterType;
import com.msc.ms.users.common.cosntans.ValueType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BooleanFilter extends FilterCriteria {
    private final  boolean value;

    @Builder
    public BooleanFilter(FilterType pFilterType,
                         ValueType pValueType, Boolean pValueBln) {
        super(pFilterType, pValueType);
        value = pValueBln;
    }
}
