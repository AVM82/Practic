package com.group.practic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "answers")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AnswerEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 6500968669125789872L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String answer;

    @Column(name = "correct")
    private boolean isCorrect;

    @ManyToOne
    private QuestionEntity question;

    public AnswerEntity(Long id, Boolean isCorrect) {
        this.id = id;
        this.isCorrect = isCorrect;
    }
}
