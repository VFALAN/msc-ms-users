package com.msc.ms.users.location;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationResponse {
    private Integer idLocality;
    private Integer idMunicipality;
    private String name;
    private String cp;
}
