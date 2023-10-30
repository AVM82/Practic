package com.group.practic.repository;

import com.group.practic.entity.StateStudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateStudentRepository  extends JpaRepository<StateStudentEntity, Long> {

    StateStudentEntity findByStudentId(long studentId);
    
}
