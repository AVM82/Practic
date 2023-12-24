package com.group.practic.repository;

import com.group.practic.entity.AnswerEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface AnswerRepository extends JpaRepository<AnswerEntity, Long> {

    @Query("SELECT a.id FROM AnswerEntity a "
            + "JOIN a.question q "
            + "WHERE a.isCorrect = true AND q.quiz.id = :quizId")
    List<Long> findAllCorrectByQuiz(@Param("quizId") Long quizId);
}
