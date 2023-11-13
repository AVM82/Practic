package com.group.practic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "answers")
public class AnswerEntity implements Serializable {

    private static final long serialVersionUID = 6500968669125789872L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String answer;

    @Column(name = "correct")
    private boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private QuestionEntity question;


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getAnswer() {
        return answer;
    }


    public void setAnswer(String answer) {
        this.answer = answer;
    }


    public boolean isCorrect() {
        return isCorrect;
    }


    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }


    public QuestionEntity getQuestion() {
        return question;
    }


    public void setQuestion(QuestionEntity question) {
        this.question = question;
    }

}
