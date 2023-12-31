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
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "chapters")
@Getter
@Setter
public class ChapterEntity implements Serializable {

    private static final long serialVersionUID = 5562887404515392104L;

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

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.MERGE)
    @OrderBy("number")
    private List<ChapterPartEntity> parts = new ArrayList<>();

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.MERGE)
    private List<TopicReportEntity> topics = new ArrayList<>();

    @OneToOne(mappedBy = "chapter", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    QuizEntity quiz;

    @JsonIgnore
    String shortName;

    private List<String> skills = new ArrayList<>();


    public ChapterEntity() {}


    public ChapterEntity(CourseEntity course, int number, String shortName, String name,
            List<String> skills) {
        this.course = course;
        this.number = number;
        this.name = name;
        this.shortName = shortName;
        this.skills = skills;
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
                && Objects.equals(shortName, other.shortName)
                && Objects.equals(skills, other.skills);
    }


    public ChapterEntity update(ChapterEntity chapter) {
        name = chapter.name;
        shortName = chapter.shortName;
        skills = chapter.skills;
        return this;
    }

}
