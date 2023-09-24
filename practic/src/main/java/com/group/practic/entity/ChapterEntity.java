package com.group.practic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "chapter")
public class ChapterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @ManyToOne
    @JsonIgnore
    CourseEntity course;

    int number;

    @NotBlank
    @Column(length = 1024)
    String name;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("number")
    Set<ChapterPartEntity> parts = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    QuizEntity quiz;

    String shortName;


    public ChapterEntity() {
    }


    public ChapterEntity(long id, CourseEntity course, int number, String shortName, String name) {
        this.id = id;
        this.course = course;
        this.number = number;
        this.name = name;
        this.shortName = shortName;
    }


    public ChapterEntity(long id, CourseEntity course, int number, String name, QuizEntity quiz) {
        this.id = id;
        this.course = course;
        this.number = number;
        this.name = name;
        this.quiz = quiz;
    }


    @Override
    public int hashCode() {
        return Objects.hash(name, number, shortName);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ChapterEntity other = (ChapterEntity) obj;
        return Objects.equals(name, other.name) && number == other.number
                && Objects.equals(shortName, other.shortName);
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public CourseEntity getCourse() {
        return course;
    }


    public void setCourse(CourseEntity course) {
        this.course = course;
    }


    public int getNumber() {
        return number;
    }


    public void setNumber(int number) {
        this.number = number;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public QuizEntity getQuiz() {
        return quiz;
    }


    public void setQuiz(QuizEntity quiz) {
        this.quiz = quiz;
    }


    public String getShortName() {
        return shortName;
    }


    public void setShortName(String shortName) {
        this.shortName = shortName;
    }


    public Set<ChapterPartEntity> getParts() {
        return parts;
    }


    public void setParts(Set<ChapterPartEntity> parts) {
        this.parts = parts;
    }

}
