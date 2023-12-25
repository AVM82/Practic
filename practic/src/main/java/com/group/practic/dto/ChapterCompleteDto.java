package com.group.practic.dto;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.ChapterPartEntity;
import com.group.practic.entity.ReportEntity;
import com.group.practic.entity.StudentChapterEntity;
import com.group.practic.entity.StudentPracticeEntity;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import lombok.Getter;


@Getter
public class ChapterCompleteDto {

    long id;

    String name;

    List<ChapterPartDto> parts;

    List<String> skills;

    List<ReportDto> reports;

    List<ReportDto> myReports;
    
    Set<Long> subs;


    public static ChapterCompleteDto map(ChapterEntity entity) {
        ChapterCompleteDto dto = new ChapterCompleteDto();
        dto.id = entity.getId();
        dto.name = entity.getName();
        dto.parts = entity.getParts().stream().map(part -> ChapterPartDto.map(part, null)).toList();
        return dto;
    }


    public static ChapterCompleteDto map(ChapterEntity entity, List<ReportEntity> reports) {
        ChapterCompleteDto dto = ChapterCompleteDto.map(entity);
        dto.skills = entity.getSkills();
        dto.reports = reports.stream().map(ReportDto::map).toList();
        return dto;
    }


    public static ChapterCompleteDto map(StudentChapterEntity entity, List<ReportEntity> reports) {
        ChapterEntity chapter = entity.getChapter();
        ChapterCompleteDto dto = new ChapterCompleteDto();
        dto.id = entity.getId();
        dto.name = chapter.getName();
        List<StudentPracticeEntity> practices = entity.getPractices();
        List<ChapterPartEntity> parts = chapter.getParts();
        dto.parts = IntStream.range(0, parts.size())
                .mapToObj(index -> ChapterPartDto.map(parts.get(index), practices.get(index)))
                .toList();
        dto.skills = chapter.getSkills();
        dto.myReports = entity.getReports().stream().map(ReportDto::map).toList();
        dto.reports = reports.stream().map(ReportDto::map).toList();
        dto.subs = entity.getSubs();
        return dto;
    }

}
