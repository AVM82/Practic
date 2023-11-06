package com.group.practic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "students",
        uniqueConstraints = {@UniqueConstraint(name = "UniqueStudentOnCourse",
                columnNames = {"person_id", "course_id"})})
@Getter
@Setter
public class StudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    boolean inactive;

    boolean ban;

    @ManyToOne
    PersonEntity person;

    @ManyToOne
    CourseEntity course;

    int activeChapterNumber;
    
    @OneToOne
    StudentChapterEntity activeChapter;

    LocalDate registered = LocalDate.now();

    LocalDate start;

    LocalDate finish;

    Integer weeks;

    String skills;

    String os;

    String english;

    String purpose;

    @OneToMany
    Set<AdditionalMaterialsEntity> additionalMaterial = new HashSet<>();

    @ManyToMany
    @OrderBy("number")
    Set<StudentChapterEntity> chapters = new HashSet<>();


    public StudentEntity() {}


    public StudentEntity(PersonEntity person, CourseEntity course) {
        this.person = person;
        this.course = course;
    }


    public StudentEntity(ApplicantEntity applicant) {
        this.person = applicant.person;
        this.course = applicant.course;
    }


    public StudentEntity(PersonEntity person, CourseEntity course, String skills, String os,
            String english, String purpose) {
        this(person, course);
        this.skills = skills;
        this.os = os;
        this.english = english;
        this.purpose = purpose;
    }


    public StudentEntity changeAdditionalMaterial(
            AdditionalMaterialsEntity additionalMaterialsEntity, boolean state) {
        if (state) {
            addAdditionalMaterial(additionalMaterialsEntity);
        } else {
            removeAdditionalMaterial(additionalMaterialsEntity);
        }
        return this;
    }


    public void addAdditionalMaterial(AdditionalMaterialsEntity additionalMaterialsEntity) {
        additionalMaterial.add(additionalMaterialsEntity);
    }


    public void removeAdditionalMaterial(AdditionalMaterialsEntity additionalMaterialsEntity) {
        additionalMaterial.remove(additionalMaterialsEntity);
    }


    public void addChapter(StudentChapterEntity chapter) {
        this.chapters.add(chapter);
        this.activeChapter = chapter;
    }


    public boolean isFinished() {
        return this.finish != null;
    }
}
