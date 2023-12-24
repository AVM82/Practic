package com.group.practic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.security.Timestamp;
import org.hibernate.annotations.CreationTimestamp;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "quiz_results")
@Getter
@Setter
public class QuizResultEntity implements Serializable {

    private static final long serialVersionUID = 6795839435418164640L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @ManyToOne
    @JsonIgnore
    StudentChapterEntity studentChapter;
    
    @CreationTimestamp
    Timestamp startedAt;
    
    int questionCount;
    
    int answeredCount;

    int correctAnsweredCount;

    boolean passed;
    
    long secondSpent;
    
}
