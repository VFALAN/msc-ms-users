package com.msc.ms.users.authentication.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogPassHistoryRequest {
    private Integer idUser;
    private String password;
}
