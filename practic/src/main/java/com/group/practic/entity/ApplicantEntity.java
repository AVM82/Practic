package com.group.practic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "applicants")
@Getter
@Setter
@NoArgsConstructor
public class ApplicantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @ManyToMany
    PersonEntity person;

    @ManyToOne
    CourseEntity course;

    @Column(columnDefinition = "boolean default false")
    boolean isApplied = false;

    
    public ApplicantEntity(PersonEntity person, CourseEntity course) {
        this.person = person;
        this.course = course;
    }
    
    
    public ApplicantEntity apply() {
        this.isApplied = true;
        return this;
    }

}
