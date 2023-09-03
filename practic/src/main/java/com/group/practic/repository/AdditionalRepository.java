package com.group.practic.repository;

import com.group.practic.entity.AdditionalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalRepository extends JpaRepository<AdditionalEntity, Long> {

    AdditionalEntity findByNumberAndName(int number, String name);
}
