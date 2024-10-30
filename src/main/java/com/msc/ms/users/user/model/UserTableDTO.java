package com.msc.ms.users.user.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTableDTO {

    private Integer idUser;
    private String name;
    private String lastName;
    private String middleName;
    private Integer profileId;
    private String profile;

}
