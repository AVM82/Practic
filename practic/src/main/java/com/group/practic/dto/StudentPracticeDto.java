package com.group.practic.dto;

import com.group.practic.enumeration.PracticeState;
import java.util.Date;

public class StudentPracticeDto {
    private String personName;
    private String chapterName;
    private String state;

    private Date updatedAt;

    public StudentPracticeDto() {
    }

    public StudentPracticeDto(String personName, String chapterName) {
        this.personName = personName;
        this.chapterName = chapterName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getState() {
        return state;
    }

    public void setState(PracticeState state) {
        this.state = state.name().toLowerCase();
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
