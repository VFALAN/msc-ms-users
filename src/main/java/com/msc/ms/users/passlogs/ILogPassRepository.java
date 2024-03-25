package com.msc.ms.users.passlogs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILogPassRepository extends JpaRepository<LogPassEntity, Integer> {
}
