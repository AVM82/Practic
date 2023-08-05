package com.group.practic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "UniqueStudentOnCourse",
        columnNames = {"student_id", "course_id"})})
@Getter
@Setter
@IdClass(StudentOnCourseId.class)
public class StudentOnCourse {

    @Id
    @ManyToOne
    StudentEntity student;

    @Id
    @ManyToOne
    @JsonIgnore
    CourseEntity course;

    Boolean inactive = false;

    Boolean ban = false;

    LocalDate registered;

    LocalDate start;

    LocalDate finish;

    Integer weeks;

    String skills;

    String os;

    String english;

    String purpose;
}
