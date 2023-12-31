package com.group.practic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mentors")
@Getter
@Setter
public class MentorEntity implements Serializable {

    private static final long serialVersionUID = 4279595070733190147L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    boolean inactive;

    @ManyToOne
    PersonEntity person;

    @ManyToOne
    CourseEntity course;

    String personPageUrl;


    public MentorEntity() {}


    public MentorEntity(PersonEntity person, CourseEntity course) {
        this.person = person;
        this.course = course;
    }


    public boolean isActive() {
        return !this.inactive;
    }

}

