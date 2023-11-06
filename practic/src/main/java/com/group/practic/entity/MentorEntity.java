package com.group.practic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

    boolean inactive;
    
    @ManyToOne
    PersonEntity person;

    @ManyToOne
    CourseEntity course;

    String linkedInUrl;


    public MentorEntity() {}


    public MentorEntity(PersonEntity person, CourseEntity course) {
        this.person = person;
        this.course = course;
    }

}

