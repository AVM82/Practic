package com.group.practic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "topic_reports")
public class TopicReportEntity implements Serializable {

    private static final long serialVersionUID = -5832117767034934071L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne
    @JsonIgnore
    private ChapterEntity chapter;

    @NotBlank
    String topic;


    public TopicReportEntity(ChapterEntity chapter, String topic) {
        this.chapter = chapter;
        this.topic = topic;
    }


    public TopicReportEntity() {}


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public ChapterEntity getChapter() {
        return chapter;
    }


    public void setChapter(ChapterEntity chapter) {
        this.chapter = chapter;
    }


    public String getTopic() {
        return topic;
    }


    public void setTopic(String topic) {
        this.topic = topic;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TopicReportEntity that = (TopicReportEntity) o;
        return id == that.id && Objects.equals(chapter, that.chapter)
                && Objects.equals(topic, that.topic);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, chapter, topic);
    }

}
