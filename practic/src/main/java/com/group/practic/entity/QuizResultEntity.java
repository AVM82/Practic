package com.group.practic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import static jakarta.persistence.CascadeType.MERGE;


@Entity
@Table(name = "quiz_results")
@Getter
@Setter
public class QuizResultEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 6795839435418164640L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @OneToOne
    @JsonIgnore
    StudentChapterEntity studentChapter;

//    @ManyToMany
//    @JoinTable(
//            name = "answer_quiz",
//            joinColumns = @JoinColumn(name = "quiz_result_id"),
//            inverseJoinColumns = @JoinColumn(name = "answer_result_id"))
//    @ManyToMany(mappedBy = "quizResult")
    @OneToMany(mappedBy = "quizResult", cascade = MERGE)
    private List<AnswerResultEntity> answerResults;

    @CreationTimestamp
    Timestamp startedAt;

    int questionCount;

    int answeredCount;

    int correctAnsweredCount;

    @Column(name = "passed", nullable = false)
    boolean passed;

    long secondSpent;

    public QuizResultEntity(StudentChapterEntity studentChapter,
                            List<AnswerResultEntity> answerResults) {
        this.studentChapter = studentChapter;
        this.answerResults = answerResults;
    }

    public QuizResultEntity(StudentChapterEntity studentChapter) {
        this.studentChapter = studentChapter;
    }

    public QuizResultEntity() {
    }
}
