package com.msc.ms.users.common.model.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DataTableRequest {

    private int page;
    private int size;
    private List<ColumnData> columns;
}
