package com.group.practic.dto;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.StudentChapterEntity;
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

    long reportCount;

    long myReports;


    public static ChapterDto map(ChapterEntity chapter, boolean hidden, long reportCount) {
        ChapterDto dto = new ChapterDto();
        dto.id = chapter.getId();
        dto.number = chapter.getNumber();
        dto.shortName = chapter.getShortName();
        dto.reportCount = reportCount;
        dto.hidden = hidden;
        return dto;
    }


    public static ChapterDto map(ChapterEntity chapter, long reportCount) {
        return ChapterDto.map(chapter, false, reportCount);
    }


    public static ChapterDto map(ChapterEntity entity) {
        return map(entity, 0);
    }


    public static Optional<ChapterDto> map(Optional<ChapterEntity> entity) {
        return entity.map(ChapterDto::map);
    }


    public static ChapterDto map(ChapterEntity chapter, boolean hidden) {
        return map(chapter, hidden, 0);
    }


    public static ChapterDto map(StudentChapterEntity studentChapter, long reportCount) {
        ChapterEntity chapter = studentChapter.getChapter();
        ChapterDto dto = new ChapterDto();
        dto.id = studentChapter.getId();
        dto.number = studentChapter.getNumber();
        dto.shortName = chapter.getShortName();
        dto.reportCount = reportCount;
        dto.state = studentChapter.getState().name();
        dto.parts = studentChapter.getPractices().stream()
                .map(prac -> ChapterPartDto.map(null, prac)).toList();
        dto.myReports = studentChapter.countNonCancelledReports();
        return dto;
    }

}
