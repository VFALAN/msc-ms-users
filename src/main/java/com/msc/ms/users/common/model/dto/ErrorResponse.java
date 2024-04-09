package com.msc.ms.users.common.model.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ErrorResponse implements Serializable {
    private String message;
    private Map<String,String> errors;
}
