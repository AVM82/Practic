package com.group.practic.repository;

import com.group.practic.entity.StateApplicantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateApplicantRepository  extends JpaRepository<StateApplicantEntity, Long> {

    StateApplicantEntity findByApplicantId(long applicantId);
    
}
