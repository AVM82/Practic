package com.group.practic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import java.util.Set;

@Entity
@Table(name = "quiz")
public class QuizEntity {

  @Id
  int id;

  @OneToMany
  @JsonIgnore
  Set<ChapterEntity> chapter;

  QuizEntity() {}

}
