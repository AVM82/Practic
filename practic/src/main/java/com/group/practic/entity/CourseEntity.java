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

@Entity
public class CourseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  long id;

  boolean inactive;

  @OneToMany(cascade = CascadeType.ALL)
  Set<String> authors = new HashSet<>();

  @ManyToMany
  Set<MentorEntity> mentors = new HashSet<>();

  String courseType;

  @NotBlank
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


  public CourseEntity() {}


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

  public Set<String> getAuthors() {
    return authors;
  }

  public void setAuthors(Set<String> authors) {
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
