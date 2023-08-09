package com.group.practic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Entity
@Table(name = "chapter")
@Data
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

  @OneToMany(cascade = CascadeType.ALL)
  @OrderBy("number")
  List<SubChapterEntity> subChapters = new ArrayList<>();

  @ManyToOne
  QuizEntity quiz;



  public ChapterEntity() {}

  
  public ChapterEntity(long id, CourseEntity course, int number, String name, QuizEntity quiz) {
    this.id = id;
    this.course = course;
    this.number = number;
    this.name = name;
    this.quiz = quiz;
  }

  
  public long getId() {
    return id;
  }

  
  public void setId(int id) {
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


  public List<SubChapterEntity> getSubChapters() {
    return subChapters;
  }


  public void setSubChapters(List<SubChapterEntity> subChapters) {
    this.subChapters = subChapters;
  }


  public void addSubChapter(SubChapterEntity subChapter) {
    subChapters.add(subChapter);
  }

}
