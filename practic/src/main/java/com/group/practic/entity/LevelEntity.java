package com.group.practic.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "levels")
public class LevelEntity implements Serializable {

    private static final long serialVersionUID = 246439068201491029L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;

    @ManyToOne
    @JsonIgnore
    CourseEntity course;

    int number;

    private List<Integer> chapterN = new ArrayList<>();

    String discordChat; // ?

    String telegramChat;

    String anotherChat;


    public LevelEntity() {
    }


    public LevelEntity(long id, CourseEntity course, int number, List<Integer> chapterN) {
        this.id = id;
        this.course = course;
        this.number = number;
        this.chapterN = chapterN;
    }


    @Override
    public int hashCode() {
        return Objects.hash(course.slug, anotherChat, chapterN, discordChat, number, telegramChat);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        LevelEntity other = (LevelEntity) obj;
        return Objects.equals(course.slug, other.course.slug)
                && Objects.equals(anotherChat, other.anotherChat)
                && Objects.equals(chapterN, other.chapterN)
                && Objects.equals(discordChat, other.discordChat) && number == other.number
                && Objects.equals(telegramChat, other.telegramChat);
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
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


    public List<Integer> getChapterN() {
        return chapterN;
    }


    public void setChapterN(List<Integer> chapterN) {
        this.chapterN = chapterN;
    }


    public String getTelegramChat() {
        return telegramChat;
    }


    public void setTelegramChat(String telegramChat) {
        this.telegramChat = telegramChat;
    }


    public String getAnotherChat() {
        return anotherChat;
    }


    public void setAnotherChat(String anotherChat) {
        this.anotherChat = anotherChat;
    }


    public String getDiscordChat() {
        return discordChat;
    }


    public void setDiscordChat(String discordChat) {
        this.discordChat = discordChat;
    }

}
