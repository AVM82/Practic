package com.group.practic.repository;

import com.group.practic.entity.AnswerResultEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerResultRepository extends JpaRepository<AnswerResultEntity, Long> {

    @Query("SELECT unnest(a.answerIds) FROM AnswerResultEntity a "
            + "WHERE a.quizResult.id=:quizResultId AND a.question.id=:questionId")
    List<Long> findByQuizResultIdAndQuestionId(@Param("quizResultId") Long quizResultId,
                                               @Param("questionId") Long questionId);
}
