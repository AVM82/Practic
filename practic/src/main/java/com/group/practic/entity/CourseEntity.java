package com.group.practic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
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
    String purpose;

    @NotBlank
    @Column(length = 8192)
    String description;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @JsonIgnore
    @OrderBy("number")
    Set<AdditionalMaterialsEntity> additionalMaterials = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @JsonIgnore
    @OrderBy("number")
    Set<LevelEntity> levels = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @JsonIgnore
    @OrderBy("number")
    Set<ChapterEntity> chapters = new HashSet<>();

    @Column(unique = true)
    @NotBlank
    String shortName;

    @Column(unique = true)
    @NotBlank
    String slug;


    public CourseEntity() {
    }


    public CourseEntity(String slug, String shortName, String name) {
        this.slug = slug;
        this.shortName = shortName;
        this.name = name;
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


    public Set<AdditionalMaterialsEntity> getAdditionalMaterials() {
        return additionalMaterials;
    }


    public void setAdditionalMaterials(Set<AdditionalMaterialsEntity> additionalMaterials) {
        this.additionalMaterials = additionalMaterials;
    }


    public Set<LevelEntity> getLevels() {
        return levels;
    }


    public void setLevels(Set<LevelEntity> levels) {
        this.levels = levels;
    }


    public Set<ChapterEntity> getChapters() {
        return chapters;
    }


    public void setChapters(Set<ChapterEntity> chapters) {
        this.chapters = chapters;
    }


    public String getShortName() {
        return shortName;
    }


    public void setShortName(String shortName) {
        this.shortName = shortName;
    }


    public String getSlug() {
        return slug;
    }


    public void setSlug(String slug) {
        this.slug = slug;
    }

}
