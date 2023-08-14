package com.group.practic.repository;

import com.group.practic.entity.SkillEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<SkillEntity, Long> {
    Optional<SkillEntity> findByName(String name);

    Optional<SkillEntity> deleteByName(String name);
}
