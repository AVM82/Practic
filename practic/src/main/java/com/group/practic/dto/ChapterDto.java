package com.group.practic.dto;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.ReportEntity;
import com.group.practic.entity.StudentChapterEntity;
import com.group.practic.entity.TopicReportEntity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import lombok.Getter;


@Getter
public class ChapterDto {

    long id;

    int number;

    boolean hidden;

    String shortName;

    List<ChapterPartDto> parts;

    String state;

    List<ReportDto> reports;

    List<TopicReportEntity> topicReports;

    List<ReportDto> myReports;


    public static ChapterDto map(ChapterEntity chapter, boolean hidden,
            List<ReportEntity> reports) {
        ChapterDto dto = new ChapterDto();
        dto.id = chapter.getId();
        dto.number = chapter.getNumber();
        dto.shortName = chapter.getShortName();
        dto.reports = reports.stream().filter(report -> report.getChapterNumber() == dto.number)
                .map(ReportDto::map).toList();
        dto.topicReports = chapter.getTopics();
        dto.hidden = hidden;
        return dto;
    }


    public static ChapterDto map(ChapterEntity chapter, List<ReportEntity> reports) {
        return ChapterDto.map(chapter, false, reports);
    }


    public static ChapterDto map(ChapterEntity entity) {
        return map(entity, List.of());
    }


    public static Optional<ChapterDto> map(Optional<ChapterEntity> entity) {
        return entity.map(ChapterDto::map);
    }


    public static ChapterDto map(ChapterEntity chapter, boolean hidden) {
        return map(chapter, hidden, List.of());
    }


    public static List<ChapterDto> map(List<ChapterEntity> chapters, boolean hidden,
            List<ReportEntity> reports) {
        List<ChapterDto> dtos = new ArrayList<>();
        Iterator<ChapterEntity> it = chapters.iterator();
        int i = 0;
        int n = reports.size();
        while (it.hasNext()) {
            ChapterEntity chapter = it.next();
            int number = chapter.getNumber();
            int j = i;
            while (j < n && reports.get(j).getChapterNumber() == number) {
                j++;
            }
            dtos.add(map(chapter, hidden, reports.subList(i, j)));
            i = j;
        }
        return dtos;
    }


    public static ChapterDto map(StudentChapterEntity studentChapter, List<ReportEntity> reports) {
        ChapterEntity chapter = studentChapter.getChapter();
        ChapterDto dto = new ChapterDto();
        dto.id = studentChapter.getId();
        dto.number = studentChapter.getNumber();
        dto.shortName = chapter.getShortName();
        dto.myReports = studentChapter.getReports().stream().map(ReportDto::map).toList();
        dto.reports = reports.stream().map(ReportDto::map).toList();
        dto.state = studentChapter.getState().name();
        dto.topicReports = chapter.getTopics();
        dto.parts = studentChapter.getPractices().stream()
                .map(prac -> ChapterPartDto.map(null, prac)).toList();
        return dto;
    }


    public static List<ChapterDto> map(List<StudentChapterEntity> studentChapters,
            List<ReportEntity> reports) {
        List<ChapterDto> dtos = new ArrayList<>();
        Iterator<StudentChapterEntity> it = studentChapters.iterator();
        int i = 0;
        int n = reports.size();
        while (it.hasNext()) {
            StudentChapterEntity chapter = it.next();
            int number = chapter.getNumber();
            int j = i;
            while (j < n && reports.get(j).getChapterNumber() == number) {
                j++;
            }
            dtos.add(map(chapter, reports.subList(i, j)));
            i = j;
        }
        return dtos;
    }



}
