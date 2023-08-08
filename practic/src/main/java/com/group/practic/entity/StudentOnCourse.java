package com.group.practic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "UniqueStudentOnCourse",
        columnNames = {"student_id", "course_id"})})
@Getter
@Setter
@NoArgsConstructor
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

    public StudentOnCourse(StudentEntity student, CourseEntity course, boolean inactive, boolean ban,
                                 LocalDate registered, LocalDate start, LocalDate finish, int weeks, String skills, String os,
                                 String english, String purpose) {
        super();
        this.student = student;
        this.course = course;
        this.inactive = inactive;
        this.ban = ban;
        this.registered = registered;
        this.start = start;
        this.finish = finish;
        this.weeks = weeks;
        this.skills = skills;
        this.os = os;
        this.english = english;
        this.purpose = purpose;
    }
}
