package com.msc.ms.users.user.model.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistryResponse {
    private Integer idUser;
    private String userName;
    private String email;
    private Date expirationDate;
}
