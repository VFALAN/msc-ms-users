package com.msc.ms.users.user.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {
    private Integer idUser;
    private String name;
    private String userName;
}
