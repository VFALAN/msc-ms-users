package com.msc.ms.users.common.model.dto;

import com.msc.ms.users.common.cosntans.SortType;
import com.msc.ms.users.common.cosntans.ValueType;
import com.msc.ms.users.common.model.dto.filters.FilterCriteria;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ColumnData {

    private Boolean isSorted;
    private Boolean isFiltered;
    private SortType sortType;
    private ValueType valueType;
    private FilterCriteria filter;
    @NotEmpty
    private String alias;
    @NotEmpty
    private String propertyPath;
}
