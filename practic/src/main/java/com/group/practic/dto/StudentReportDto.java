package com.group.practic.dto;

import com.group.practic.entity.StudentReportEntity;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentReportDto {

    private long id;

    private String personName;

    private long personId;

    private String profilePictureUrl;

    private String chapterName;

    private long chapterId;

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


    public static StudentReportDto map(StudentReportEntity report) {
        StudentReportDto dto = new StudentReportDto();
        dto.id = report.getId();
        dto.personName = report.getStudentChapter().getStudent().getPerson().getName();
        dto.personId = report.getStudentChapter().getStudent().getId();
        dto.profilePictureUrl = report.getStudentChapter()
                .getStudent().getPerson().getProfilePictureUrl();
        dto.chapterName = report.getStudentChapter().getChapter().getShortName();
        dto.chapterId = report.getStudentChapter().getChapter().getId();
        dto.date = report.getTimeSlot().getDate();
        dto.time = report.getTimeSlot().getTime();
        dto.timeslotId = report.getTimeSlot().getId();
        dto.state = report.getState().name();
        dto.title = report.getTitle();
        dto.likedPersonsIdList = report.getLikedPersonsIdList();
        return dto;
    }

}
