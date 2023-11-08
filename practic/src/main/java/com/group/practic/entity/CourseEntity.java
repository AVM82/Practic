package com.group.practic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    boolean inactive;

    Set<String> authors;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Set<MentorEntity> mentors = new HashSet<>();

    String courseType;

    @Min(5)
    String name;

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
    @Min(5)
    String slug;

    @Column(length = 16384)
    String svg;

    boolean additionalMaterialsExist;


    public CourseEntity() {}


    public CourseEntity(String slug, String name, String svg) {
        this.slug = slug;
        this.name = name;
        this.svg = svg;
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


    public Set<String> getAuthors() {
        return authors;
    }


    public void setAuthors(Set<String> authors) {
        this.authors = authors;
    }


    public Set<MentorEntity> getMentors() {
        return mentors;
    }


    public void setMentors(Set<MentorEntity> mentors) {
        this.mentors = mentors;
    }


    public boolean setMentor(MentorEntity mentor) {
        return mentors.add(mentor);
    }


    public boolean removeMentor(MentorEntity mentor) {
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
        this.additionalMaterialsExist =
                additionalMaterials != null && !additionalMaterials.isEmpty();
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


    public String getSlug() {
        return slug;
    }


    public void setSlug(String slug) {
        this.slug = slug;
    }


    public String getSvg() {
        return svg;
    }


    public void setSvg(String svg) {
        this.svg = svg;
    }


    public boolean isAdditionalMaterialsExist() {
        return additionalMaterialsExist;
    }


    public void setAdditionalMaterialsExist(boolean additionalMaterialsExist) {
        this.additionalMaterialsExist = additionalMaterialsExist;
    }

}
