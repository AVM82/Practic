package com.group.practic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.Data;


@Data
@Entity
@Table(name = "answer_result")
public class AnswerResultEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 6390825978859428396L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    private QuizResultEntity quizResult;

    @ManyToOne
    private QuestionEntity question;

    private List<Long> answerIds;

}
