package com.group.practic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "feedbacks")
public class FeedbackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne
    private PersonEntity person;

    @NotBlank
    @Column(length = 320)
    private String feedback;

    private LocalDateTime dateTime = LocalDateTime.now();

    private int likes;

    private Set<Long> likedByPerson = new HashSet<>();


    public FeedbackEntity() {}


    public FeedbackEntity(PersonEntity student, String feedback) {
        this.person = student;
        this.feedback = feedback;
    }


    public FeedbackEntity(PersonEntity student, String feedback, int likes) {
        this.person = student;
        this.feedback = feedback;
        this.likes = likes;
    }

}
