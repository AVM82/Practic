package com.group.practic.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "courses")
public class CourseEntity implements Serializable {

    private static final long serialVersionUID = -1132517329070397053L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    boolean inactive;

    private Set<String> authors;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "courses_mentors", joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "mentor_id"))
    private Set<MentorEntity> mentors = new HashSet<>();

    String courseType;

    String name;

    @Column(length = 8192)
    String description;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    // @JoinTable(name = "courses_add_mats", joinColumns = @JoinColumn(name = "course_id"),
    // inverseJoinColumns = @JoinColumn(name = "add_mat_id"))
    @OrderBy("number")
    private List<AdditionalMaterialsEntity> additionalMaterials = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    // @JoinTable(name = "courses_levels", joinColumns = @JoinColumn(name = "course_id"),
    // inverseJoinColumns = @JoinColumn(name = "level_id"))
    @OrderBy("number")
    private List<LevelEntity> levels = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    // @JoinTable(name = "courses_chapters", joinColumns = @JoinColumn(name = "course_id"),
    // inverseJoinColumns = @JoinColumn(name = "chapter_id"))
    @OrderBy("number")
    private List<ChapterEntity> chapters = new ArrayList<>();

    @Column(unique = true)
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


    public List<AdditionalMaterialsEntity> getAdditionalMaterials() {
        return additionalMaterials;
    }


    public void setAdditionalMaterials(List<AdditionalMaterialsEntity> additionalMaterials) {
        this.additionalMaterials = additionalMaterials;
        this.additionalMaterialsExist =
                additionalMaterials != null && !additionalMaterials.isEmpty();
    }


    public List<LevelEntity> getLevels() {
        return levels;
    }


    public void setLevels(List<LevelEntity> levels) {
        this.levels = levels;
    }


    public List<ChapterEntity> getChapters() {
        return chapters;
    }


    public void setChapters(List<ChapterEntity> chapters) {
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
