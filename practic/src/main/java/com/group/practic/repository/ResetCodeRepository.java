package com.group.practic.repository;

import com.group.practic.entity.ResetCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ResetCodeRepository extends JpaRepository<ResetCodeEntity, Long> {

    @Query("SELECT r FROM ResetCodeEntity r WHERE r.person.email = :email")
    ResetCodeEntity findByPersonEmail(@Param("email") String email);

    ResetCodeEntity findByCode(String code);

}
