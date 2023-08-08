package com.group.practic.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    Boolean inactive;

    @OneToMany(cascade = CascadeType.ALL)
    Set<AuthorEntity> authors = new HashSet<>();

    @ManyToMany
    Set<MentorEntity> mentors = new HashSet<>();

    String courseType;

    String name;

    @Column(length = 1024)
    String purpose;

    @NotBlank
    @Column(length = 8192)
    String description;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<StudentOnCourse> students = new HashSet<>();

    @ManyToOne
    ChapterEntity additionalMaterials;

    public CourseEntity(String name, String description) {
        this.name = name;
        this.description = description;
        mentors = Set.of();
    }

    public CourseEntity(Long id, Boolean inactive, Set<AuthorEntity> authors,
                        Set<MentorEntity> mentors, String courseType, String name,
                        String purpose, String description, ChapterEntity additionalMaterials) {
        super();
        this.id = id;
        this.inactive = inactive;
        this.authors = authors;
        this.mentors = mentors;
        this.courseType = courseType;
        this.name = name;
        this.purpose = purpose;
        this.description = description;
        this.additionalMaterials = additionalMaterials;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Boolean getInactive() {
        return inactive;
    }


    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }

    public Set<AuthorEntity> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<AuthorEntity> authors) {
        this.authors = authors;
    }

    public Set<MentorEntity> getMentors() {
        return mentors;
    }


    public void setMentors(Set<MentorEntity> mentor) {
        this.mentors = mentor;
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
}
