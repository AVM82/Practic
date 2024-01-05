package com.group.practic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "graduates")
@NoArgsConstructor
@Getter
@Setter
public class GraduateEntity implements Serializable {

    private static final long serialVersionUID = -1490774124799301954L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @OneToOne
    StudentEntity student;

    String slug;

    @ManyToOne
    PersonEntity person;

    @ManyToOne
    CourseEntity course;

    LocalDate start;

    LocalDate finish;

    @Column(name = "weeks")
    int weeks;

    @Column(name = "days_spent")
    int daysSpent;

    private Set<String> skills;


    public GraduateEntity(StudentEntity student) {
        this.student = student;
        person = student.person;
        course = student.course;
        start = student.start;
        finish = student.finish;
        weeks = student.weeks;
        daysSpent = student.daysSpent;
        skills = student.getSkills();
    }

}
