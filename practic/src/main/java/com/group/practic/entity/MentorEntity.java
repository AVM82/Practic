package com.group.practic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mentors")
@Getter
@Setter
public class MentorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @ManyToOne
    PersonEntity person;

    @ManyToOne
    CourseEntity course;

    String linkedInUrl;


    public MentorEntity() {}


    public MentorEntity(PersonEntity person, CourseEntity course, String linkedInUrl) {
        this.person = person;
        this.course = course;
        this.linkedInUrl = linkedInUrl;
    }

}

