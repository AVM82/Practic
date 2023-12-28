package com.group.practic.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "students",
        uniqueConstraints = {@UniqueConstraint(name = "UniqueStudentOnCourse",
                columnNames = {"person_id", "course_id"})})
@Getter
@Setter
public class StudentEntity implements Serializable {

    private static final long serialVersionUID = -7778946871629233665L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    boolean inactive;

    boolean ban;

    @ManyToOne
    PersonEntity person;

    @ManyToOne
    CourseEntity course;

    @Column(name = "active_chapter_number")
    int activeChapterNumber;

    LocalDate registered = LocalDate.now();

    LocalDate start;

    LocalDate finish;

    @Column(name = "weeks")
    int weeks;

    @Column(name = "days_spent")
    int daysSpent;

    private Set<String> skills = new HashSet<>();

    String os;

    String english;

    String purpose;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "students_student_add_mats", joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "add_mat_id"))
    @OrderBy("number")
    private List<AdditionalMaterialsEntity> additionalMaterials = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.MERGE)
    @OrderBy("number")
    private List<StudentChapterEntity> studentChapters = new ArrayList<>();


    public StudentEntity() {}


    public StudentEntity(PersonEntity person, CourseEntity course) {
        this.person = person;
        this.course = course;
    }


    public StudentEntity(ApplicantEntity applicant) {
        this.person = applicant.person;
        this.course = applicant.course;
    }


    public StudentEntity changeAdditionalMaterial(
            AdditionalMaterialsEntity additionalMaterialsEntity, boolean state) {
        if (state) {
            additionalMaterials.add(additionalMaterialsEntity);
        } else {
            additionalMaterials.remove(additionalMaterialsEntity);
        }
        return this;
    }


    public boolean isFinished() {
        return this.finish != null;
    }

}
