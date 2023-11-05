package com.group.practic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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

    @ManyToOne
    PersonEntity person;

    @ManyToOne
    CourseEntity course;
    
    @Column(columnDefinition = "boolean default false")
    boolean isApplied = false;

    LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    StudentEntity student;
    
    
    public ApplicantEntity(PersonEntity person, CourseEntity course) {
        this.person = person;
        this.course = course;
    }
    
    
    public ApplicantEntity apply(StudentEntity student) {
        this.isApplied = true;
        this.student = student;
        return this;
    }

}
