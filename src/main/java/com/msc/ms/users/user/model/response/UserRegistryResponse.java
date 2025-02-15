package com.msc.ms.users.user.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistryResponse {
    private Integer idUser;
    private String userName;
    private String email;
}
