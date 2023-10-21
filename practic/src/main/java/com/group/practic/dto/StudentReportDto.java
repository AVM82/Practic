package com.group.practic.dto;

import com.group.practic.entity.StudentReportEntity;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class StudentReportDto {

    private long id;

    private String personName;

    private long personId;

    private String profilePictureUrl;

    private String chapterName;

    private LocalDate date;

    private LocalTime time;

    private long timeslotId;

    private String state;

    private String title;

    private List<Long> likedPersonsIdList;


    public StudentReportDto() {}


    public StudentReportDto(String personName, String chapterName) {
        this.personName = personName;
        this.chapterName = chapterName;
    }


    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }


    public String getPersonName() {
        return personName;
    }


    public void setPersonName(String personName) {
        this.personName = personName;
    }


    public long getPersonId() {
        return personId;
    }


    public void setPersonId(long personId) {
        this.personId = personId;
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


    public long getTimeslotId() {
        return timeslotId;
    }


    public void setTimeslotId(long timeslotId) {
        this.timeslotId = timeslotId;
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


    public List<Long> getLikedPersonsIdList() {
        return likedPersonsIdList;
    }


    public void setLikedPersonsIdList(List<Long> likedPersonsIdList) {
        this.likedPersonsIdList = likedPersonsIdList;
    }


    public static StudentReportDto map(StudentReportEntity report) {
        StudentReportDto dto = new StudentReportDto();
        dto.id = report.getId();
        dto.personName = report.getStudent().getName();
        dto.personId = report.getStudent().getId();
        dto.profilePictureUrl = report.getStudent().getProfilePictureUrl();
        dto.chapterName = report.getChapter().getShortName();
        dto.date = report.getTimeSlot().getDate();
        dto.time = report.getTimeSlot().getTime();
        dto.timeslotId = report.getTimeSlot().getId();
        dto.state = report.getState().name();
        dto.title = report.getTitle();
        dto.likedPersonsIdList = report.getLikedPersonsIdList();
        return dto;
    }

}
