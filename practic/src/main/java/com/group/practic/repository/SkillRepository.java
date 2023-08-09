package com.group.practic.repository;

import com.group.practic.entity.SkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<SkillEntity, Long>{
    SkillEntity findByName(String name);

    SkillEntity deleteByName(String name);
}
