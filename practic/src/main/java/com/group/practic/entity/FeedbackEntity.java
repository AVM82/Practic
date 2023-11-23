package com.group.practic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
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

    private long personId;

    private String name;

    private  String profilePictureUrl;

    @Size(min = 5, max = 320)
    @Column(length = 320)
    private String feedback;

    private LocalDateTime dateTime = LocalDateTime.now();

    private int likes;

    private Set<Long> likedByPerson = new HashSet<>();

    public FeedbackEntity() {}


    public FeedbackEntity(PersonEntity person, String feedback) {
        this.personId = person.id;
        this.name = person.name;
        this.profilePictureUrl = person.profilePictureUrl;
        this.feedback = feedback;
    }
}
