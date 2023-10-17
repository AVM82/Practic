package com.group.practic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "student_on_course",
        uniqueConstraints = {@UniqueConstraint(name = "UniqueStudentOnCourse",
                columnNames = {"student_id", "course_id"})})
public class StudentOnCourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @ManyToOne
    PersonEntity student;

    @ManyToOne
    ChapterEntity activeChapter;

    @ManyToOne
    CourseEntity course;

    boolean inactive;

    boolean ban;

    LocalDate registered;

    LocalDate start;

    LocalDate finish;

    Integer weeks;

    String skills;

    String os;

    String english;

    String purpose;

    @OneToMany
    Set<AdditionalMaterialsEntity> additionalMaterial = new HashSet<>();


    public StudentOnCourseEntity() {}


    public StudentOnCourseEntity(PersonEntity student, CourseEntity course) {
        this.student = student;
        this.course = course;
        this.registered = LocalDate.now();
    }


    public StudentOnCourseEntity(PersonEntity student, CourseEntity course, String skills,
            String os, String english, String purpose) {
        this(student, course);
        this.skills = skills;
        this.os = os;
        this.english = english;
        this.purpose = purpose;
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


    public CourseEntity getCourse() {
        return course;
    }


    public void setCourse(CourseEntity course) {
        this.course = course;
    }


    public Boolean getInactive() {
        return inactive;
    }


    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }


    public Boolean getBan() {
        return ban;
    }


    public void setBan(Boolean ban) {
        this.ban = ban;
    }


    public LocalDate getStart() {
        return start;
    }


    public void setStart(LocalDate start) {
        this.start = start;
    }


    public LocalDate getFinish() {
        return finish;
    }


    public void setFinish(LocalDate finish) {
        this.finish = finish;
    }


    public Integer getWeeks() {
        return weeks;
    }


    public void setWeeks(Integer weeks) {
        this.weeks = weeks;
    }


    public String getSkills() {
        return skills;
    }


    public void setSkills(String skills) {
        this.skills = skills;
    }


    public String getOs() {
        return os;
    }


    public void setOs(String os) {
        this.os = os;
    }


    public String getEnglish() {
        return english;
    }


    public void setEnglish(String english) {
        this.english = english;
    }


    public String getPurpose() {
        return purpose;
    }


    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }


    public LocalDate getRegistered() {
        return registered;
    }


    public ChapterEntity getActiveChapter() {
        return activeChapter;
    }


    public StudentOnCourseEntity setActiveChapter(ChapterEntity activeChapter) {
        this.activeChapter = activeChapter;
        return this;
    }


    public Set<AdditionalMaterialsEntity> getAdditionalMaterial() {
        return additionalMaterial;
    }


    public void setAdditionalMaterial(Set<AdditionalMaterialsEntity> additionalMaterial) {
        this.additionalMaterial = additionalMaterial;
    }


    public void changeAdditionalMaterial(AdditionalMaterialsEntity additionalMaterialsEntity,
            boolean state) {
        if (state) {
            addAdditionalMaterial(additionalMaterialsEntity);
        } else {
            removeAdditionalMaterial(additionalMaterialsEntity);
        }
    }


    public void addAdditionalMaterial(AdditionalMaterialsEntity additionalMaterialsEntity) {
        additionalMaterial.add(additionalMaterialsEntity);
    }


    public void removeAdditionalMaterial(AdditionalMaterialsEntity additionalMaterialsEntity) {
        additionalMaterial.remove(additionalMaterialsEntity);
    }

}
