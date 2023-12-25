package com.group.practic.repository;

import com.group.practic.entity.ResetCodeEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ResetCodeRepository extends JpaRepository<ResetCodeEntity, Long> {

    Optional<ResetCodeEntity> findByEmail(String email);

    Optional<ResetCodeEntity> findByCode(String code);

}
