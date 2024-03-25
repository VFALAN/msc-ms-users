package com.msc.ms.users.passlogs;

import com.msc.ms.users.common.model.entity.AuditEntity;
import com.msc.ms.users.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "TLOG_PASS")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogPassEntity extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LOG_PASS")
    private Integer idLogPass;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "EXPIRED")
    private Boolean expired;

    @JoinColumn(name = "ID_USER")
    @ManyToOne
    private UserEntity idUser;


}