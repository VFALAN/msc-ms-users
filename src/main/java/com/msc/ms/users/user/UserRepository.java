package com.msc.ms.users.user;

import com.msc.ms.users.user.model.UserEntity;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    @Procedure("search_username")
    Integer searchUsername(@Param("username") String pUsername);
}
