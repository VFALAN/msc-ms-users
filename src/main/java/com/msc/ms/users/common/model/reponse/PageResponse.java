package com.msc.ms.users.common.model.reponse;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponse<T> {


    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private List<T> content;
}
