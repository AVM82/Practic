package com.group.practic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "quiz")
public class QuizEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;
    @Column(name = "chapter_name")
    private String chapterName;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<QuestionEntity> questions;

    @OneToMany
    @JsonIgnore
    Set<ChapterEntity> chapter;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public List<QuestionEntity> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionEntity> questions) {
        this.questions = questions;
    }

    public Set<ChapterEntity> getChapter() {
        return chapter;
    }

    public void setChapter(Set<ChapterEntity> chapter) {
        this.chapter = chapter;
    }
}
