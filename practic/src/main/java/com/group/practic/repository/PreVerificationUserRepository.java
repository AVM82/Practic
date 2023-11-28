package com.group.practic.repository;

import com.group.practic.entity.PreVerificationUserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreVerificationUserRepository extends
        JpaRepository<PreVerificationUserEntity, Long> {
    Optional<PreVerificationUserEntity> findByToken(String token);

}
