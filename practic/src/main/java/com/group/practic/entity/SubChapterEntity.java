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
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sub_chapter")
public class SubChapterEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  ChapterEntity chapter;

  int number;

  @NotBlank
  @Column(length = 1024)
  String name;

  @Column(length = 1024)
  String refs;

  @OneToMany(cascade = CascadeType.ALL)
  @OrderBy("number")
  List<SubSubChapterEntity> subSubChapters = new ArrayList<>();


  public SubChapterEntity() {}


  public SubChapterEntity(int id, ChapterEntity chapter, int number, String name, String refs) {
    super();
    this.id = id;
    this.chapter = chapter;
    this.number = number;
    this.name = name;
    this.refs = refs;
  }


  public long getId() {
    return id;
  }


  public void setId(int id) {
    this.id = id;
  }


  public ChapterEntity getChapter() {
    return chapter;
  }


  public void setChapter(ChapterEntity chapter) {
    this.chapter = chapter;
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


  public String getRefs() {
    return refs;
  }


  public void setRefs(String refs) {
    this.refs = refs;
  }


  public List<SubSubChapterEntity> getSubSubChapters() {
    return subSubChapters;
  }


  public void setSubSubChapters(List<SubSubChapterEntity> subSubChapters) {
    this.subSubChapters = subSubChapters;
  }


  public void addSubSubChapter(SubSubChapterEntity subSubChapter) {
    subSubChapters.add(subSubChapter);
  }


  public boolean removeSubSubChapter(SubSubChapterEntity subSubChapter) {
    return subSubChapters.remove(subSubChapter);
  }

}
