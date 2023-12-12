package com.group.practic.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "courses")
@Getter
@Setter
public class CourseEntity implements Serializable {

    private static final long serialVersionUID = -1132517329070397053L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @Column(unique = true)
    String slug;

    boolean inactive;

    private Set<String> authors;

    @OneToMany(mappedBy = "course", cascade = CascadeType.MERGE)
    private Set<MentorEntity> mentors = new HashSet<>();

    String courseType;

    String name;

    @Column(length = 8192)
    String description;

    @OneToMany(mappedBy = "course", cascade = CascadeType.MERGE)
    @OrderBy("number")
    private List<AdditionalMaterialsEntity> additionalMaterials = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.MERGE)
    @OrderBy("number")
    private List<LevelEntity> levels = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.MERGE)
    @OrderBy("number")
    private List<ChapterEntity> chapters = new ArrayList<>();

    @Column(length = 16384)
    String svg;

    boolean additionalMaterialsExist;


    public CourseEntity() {}


    public CourseEntity(String slug, String name, String svg) {
        this.slug = slug;
        this.name = name;
        this.svg = svg;
    }


    public void setAdditionalMaterials(List<AdditionalMaterialsEntity> additionalMaterials) {
        this.additionalMaterials = additionalMaterials;
        this.additionalMaterialsExist =
                additionalMaterials != null && !additionalMaterials.isEmpty();
    }


    public CourseEntity update(CourseEntity course) {
        inactive = course.inactive;
        authors = course.authors;
        courseType = course.courseType;
        name = course.name;
        description = course.description;
        svg = course.svg;
        return this;
    }


    @Override
    public int hashCode() {
        return Objects.hash(slug, inactive, authors, mentors, courseType, name, description, svg,
                levels, chapters, additionalMaterials);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CourseEntity other = (CourseEntity) obj;
        return this == other || (Objects.equals(slug, other.slug)
                && Objects.equals(authors, other.authors) && Objects.equals(name, other.name)
                && Objects.equals(courseType, other.courseType) && Objects.equals(svg, other.svg)
                && Objects.equals(description, other.description));
    }

}
