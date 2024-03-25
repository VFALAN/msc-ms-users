package com.msc.ms.users.user.model;

import com.msc.ms.users.address.AddressEntity;
import com.msc.ms.users.common.model.entity.AuditEntity;
import com.msc.ms.users.profile.ProfileEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Table(name = "TUSER")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USER")
    @Basic(optional = false)
    private Integer idUser;
    @Column(name = "NAME")
    @Basic(optional = false)
    private String name;
    @Column(name = "LAST_NAME")
    private String lastName;
    @Column(name = "MIDDLE_NAME")
    private String middleName;
    @Column(name = "BIRTH_DATE")
    private Date birthDate;
    @Column(name = "AGE")
    private Integer age;
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "USER_NAME")
    private String userName;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ADDRESS")
    private AddressEntity address;
    @JoinColumn(name = "PROFILE")
    @ManyToOne
    private ProfileEntity profile;

}
