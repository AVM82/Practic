package com.group.practic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;


@Entity
@Table(name = "quiz_results")
@Getter
@Setter
@NoArgsConstructor
public class QuizResultEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 6795839435418164640L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @ManyToOne
    @JsonIgnore
    StudentChapterEntity studentChapter;

    @CreationTimestamp
    Timestamp startedAt;

    @Column(name = "question_count")
    int questionCount;

    @Column(name = "answered_count")
    int answeredCount;

    @Column(name = "correct_answered_count")
    int correctAnsweredCount;

    @Column(name = "passed", nullable = false)
    boolean passed;

    @Column(name = "second_spent")
    long secondSpent;


    public QuizResultEntity(StudentChapterEntity studentChapter) {
        this.studentChapter = studentChapter;
    }

}
