package com.group.practic.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "feedback")
public class FeedbackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne
    private PersonEntity student;
    @NotBlank
    @Min(5)
    @Column(length = 320)
    private String feedback;

    private int likes;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JoinTable(
            name = "feedback_likes",
            joinColumns = @JoinColumn(name = "feedback_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "person_id", referencedColumnName = "id")
    )
    Set<PersonEntity> likedByPerson = new HashSet<>();


    public FeedbackEntity(PersonEntity student, String feedback, int likes) {
        this.student = student;
        this.feedback = feedback;
        this.likes = likes;
    }

    public FeedbackEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PersonEntity getStudent() {
        return student;
    }

    public void setStudent(PersonEntity student) {
        this.student = student;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }


    public Set<PersonEntity> getLikedByPerson() {
        return likedByPerson;
    }

    public void setLikedByPerson(HashSet<PersonEntity> likedByPerson) {
        this.likedByPerson = likedByPerson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedbackEntity feedback1 = (FeedbackEntity) o;
        return id == feedback1.id && likes == feedback1.likes && Objects.equals(student, feedback1.student) && Objects.equals(feedback, feedback1.feedback) && Objects.equals(likedByPerson, feedback1.likedByPerson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, student, feedback, likes, likedByPerson);
    }
}