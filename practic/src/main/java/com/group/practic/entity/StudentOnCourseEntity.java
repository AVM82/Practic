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
import lombok.Setter;


@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(name = "UniqueStudentOnCourse", columnNames = {"student_id", "course_id"})})
@Getter
@Setter
@IdClass(StudentOnCourseId.class)
public class StudentOnCourseEntity {
  
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


  public StudentOnCourseEntity() {}


  public StudentOnCourseEntity(StudentEntity student, CourseEntity course, String skills, String os,
      String english, String purpose) {
    this.student = student;
    this.course = course;
    this.registered = LocalDate.now();
    this.skills = skills;
    this.os = os;
    this.english = english;
    this.purpose = purpose;
  }

}
