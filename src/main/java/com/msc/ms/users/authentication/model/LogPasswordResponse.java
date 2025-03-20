package com.msc.ms.users.authentication.model;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogPasswordResponse implements Serializable {
    private Date expirationDate;
    private String username;
}
