package com.group.practic.dto;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.ChapterPartEntity;
import com.group.practic.entity.QuizResultEntity;
import com.group.practic.entity.StudentChapterEntity;
import com.group.practic.entity.StudentPracticeEntity;
import com.group.practic.entity.TopicReportEntity;
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

    long reportCount;

    List<StudentReportDto> reports;

    Set<Long> subs;

    List<String> topicReports;

    Long quizResultId;


    public static ChapterCompleteDto map(ChapterEntity entity) {
        ChapterCompleteDto dto = new ChapterCompleteDto();
        dto.id = entity.getId();
        dto.name = entity.getName();
        dto.parts = entity.getParts().stream().map(part -> ChapterPartDto.map(part, null)).toList();
        return dto;
    }


    public static ChapterCompleteDto map(ChapterEntity entity, long reportCount) {
        ChapterCompleteDto dto = ChapterCompleteDto.map(entity);
        dto.reportCount = reportCount;
        dto.skills = entity.getSkills();
        dto.topicReports = entity.getTopics().stream().map(TopicReportEntity::getTopic).toList();
        return dto;
    }


    public static ChapterCompleteDto map(StudentChapterEntity entity, long reportCount) {
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
        dto.reportCount = reportCount;
        dto.reports = entity.getReports().stream().map(StudentReportDto::map).toList();
        dto.subs = entity.getSubs();
        dto.topicReports = chapter.getTopics().stream().map(TopicReportEntity::getTopic).toList();
        QuizResultEntity quizResult = entity.getQuizResult();
        dto.quizResultId = quizResult != null ? quizResult.getId() : null;
        return dto;
    }

}
