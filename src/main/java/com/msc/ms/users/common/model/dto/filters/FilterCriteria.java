package com.msc.ms.users.common.model.dto.filters;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.msc.ms.users.common.cosntans.FilterType;
import com.msc.ms.users.common.cosntans.ValueType;
import com.msc.ms.users.common.utils.FilterCriteriaDeserializer;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonDeserialize(using = FilterCriteriaDeserializer.class)
public abstract class FilterCriteria {
    @NotEmpty(message = "FilterType is required")
    private FilterType filterType;
    @NotEmpty(message = "Value is required")
    private ValueType valueType;



}
