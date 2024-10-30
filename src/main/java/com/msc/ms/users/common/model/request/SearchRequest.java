package com.msc.ms.users.common.model.request;

import com.msc.ms.users.common.model.dto.ColumnData;
import lombok.Data;

import java.util.List;

@Data
public class SearchRequest {
    private int page;
    private int size;
    private List<String> fields;
    private List<ColumnData> columns;
}
