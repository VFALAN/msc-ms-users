package com.msc.ms.users.common.model.dto.filters;

import com.msc.ms.users.common.cosntans.FilterType;
import com.msc.ms.users.common.cosntans.ValueType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class StringFilter extends FilterCriteria {
    private final String regex;
    private final String value;

    @Builder
    public StringFilter(FilterType pFilterType,
                        ValueType pValueType,

                        String pValueStr,
                        String pRegexStr) {
        super(pFilterType, pValueType);
        value = pValueStr;
        regex = pRegexStr;
    }
}
