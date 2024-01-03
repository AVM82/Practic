package com.group.practic.dto;

import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.ReportEntity;
import com.group.practic.entity.TopicReportEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import lombok.Getter;


@Getter
public class ReportDto {

    long id;

    long personId;

    String personName;

    String profilePictureUrl;

    int chapterNumber;
    
    LocalDate date;

    TopicReportEntity topic;

    long studentChapterId;

    String state;

    Set<Long> likedPersonIds;


    public static ReportDto map(ReportEntity report) {
        if (report == null) {
            return null;
        }
        ReportDto dto = new ReportDto();
        dto.id = report.getId();
        if (dto.id != 0) {
            PersonEntity person = report.getPerson();
            dto.personId = person.getId();
            dto.personName = person.getName();
            dto.profilePictureUrl = person.getProfilePictureUrl();
            dto.chapterNumber = report.getChapterNumber();
            dto.date = report.getDate();
            dto.topic = report.getTopic();
            dto.state = report.getState().name();
            dto.likedPersonIds = report.getLikedPersonIds();
            dto.studentChapterId =
                    report.getStudentChapter() != null ? report.getStudentChapter().getId() : 0L;
        }
        return dto;
    }


    public static List<ReportDto> map(List<ReportEntity> list) {
        return list.stream().map(ReportDto::map).toList();
    }

}
