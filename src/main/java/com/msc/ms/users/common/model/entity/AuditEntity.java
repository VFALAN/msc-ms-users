package com.msc.ms.users.common.model.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.util.Date;

@MappedSuperclass
@Data
public class AuditEntity {
    private Date dateCreate;
    private Date dateUpdate;
    private Date dateDelete;
    private Boolean active;

}
