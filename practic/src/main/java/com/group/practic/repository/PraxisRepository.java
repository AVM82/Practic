package com.group.practic.repository;

import com.group.practic.entity.PraxisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PraxisRepository extends JpaRepository<PraxisEntity, Long> {

    PraxisEntity findByNumberAndName(int number, String name);
}
