package com.group.practic.repository;

import com.group.practic.entity.QuizResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface QuizResultRepository extends JpaRepository<QuizResultEntity, Long> {
}
