package com.msc.ms.users.profile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IProfileRepository extends JpaRepository<ProfileEntity, Integer> {
    @Query("SELECT p FROM ProfileEntity p where p.keyProfile =:key")
    Optional<ProfileEntity> findByKey(@Param("key") String key);

}
