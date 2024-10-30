package com.msc.ms.users.common.model.dto;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DynamicDataTableResponse<T> {
    private int totalPages;
    private long totalRecords;
    private long currentPage;
    private T data;

}
