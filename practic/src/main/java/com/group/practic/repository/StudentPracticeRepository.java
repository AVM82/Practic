package com.group.practic.repository;

import com.group.practic.entity.StudentPracticeEntity;
import com.group.practic.enumeration.PracticeState;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentPracticeRepository extends JpaRepository<StudentPracticeEntity, Long> {

    List<StudentPracticeEntity> findByState(PracticeState state);
}