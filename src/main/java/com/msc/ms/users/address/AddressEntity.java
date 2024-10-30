package com.msc.ms.users.address;

import com.msc.ms.users.common.model.entity.AuditEntity;
import com.msc.ms.users.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TADDRESS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Builder
public class AddressEntity extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ADDRESS")
    private Integer idAddress;
    @Column(name = "STREET")
    private String street;
    @Column(name = "DESCRIPTION")
    private String description;
    @OneToOne(mappedBy = "address")
    private UserEntity user;
    @JoinColumn(name = "ID_LOCATION")
    private Integer idLocation;
}
