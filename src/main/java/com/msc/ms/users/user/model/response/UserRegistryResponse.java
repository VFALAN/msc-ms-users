package com.msc.ms.users.user.model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistryResponse {
    private Integer idUser;
    private String username;
    private String email;
}
