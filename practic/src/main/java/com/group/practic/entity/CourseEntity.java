package com.group.practic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "course")
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    boolean inactive;

    String authors;

    @ManyToMany
    Set<PersonEntity> mentors = new HashSet<>();

    String courseType;

    @NotBlank
    String name;

    @Column(length = 1024)
    @JsonIgnore
    String purpose;

    @NotBlank
    @Column(length = 8192)
    String description;

    @ManyToOne
    @JsonIgnore
    ChapterEntity additionalMaterials;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @JsonIgnore
    List<ChapterEntity> chapters = new ArrayList<>();


    public CourseEntity() {
    }


    public CourseEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public boolean getInactive() {
        return inactive;
    }


    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }


    public String getAuthors() {
        return authors;
    }


    public void setAuthors(String authors) {
        this.authors = authors;
    }


    public Set<PersonEntity> getMentors() {
        return mentors;
    }


    public void setMentors(Set<PersonEntity> mentors) {
        this.mentors = mentors;
    }


    public boolean setMentor(PersonEntity mentor) {
        return mentors.add(mentor);
    }


    public boolean removeMentor(PersonEntity mentor) {
        return mentors.remove(mentor);
    }


    public String getCourseType() {
        return courseType;
    }


    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getPurpose() {
        return purpose;
    }


    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public ChapterEntity getAdditionalMaterials() {
        return additionalMaterials;
    }


    public void setAdditionalMaterials(ChapterEntity additionalMaterials) {
        this.additionalMaterials = additionalMaterials;
    }


    public List<ChapterEntity> getChapters() {
        return chapters;
    }


    public void setChapters(List<ChapterEntity> chapters) {
        this.chapters = chapters;
    }

}
