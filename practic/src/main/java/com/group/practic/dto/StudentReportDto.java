package com.group.practic.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class StudentReportDto {
    private String personName;
    private String profilePictureUrl;
    private String chapterName;
    private LocalDate date;
    private LocalTime time;
    private String state;
    private String title;

    public StudentReportDto() {
    }

    public StudentReportDto(String personName, String chapterName) {
        this.personName = personName;
        this.chapterName = chapterName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
