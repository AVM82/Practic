package com.group.practic.dto;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.StudentChapterEntity;
import java.util.List;
import lombok.Getter;
import org.springframework.context.annotation.Bean;


@Getter
public class ShortChapterDto {

    long id;

    int number;

    String shortName;

    int reportCount;

    boolean hidden;

    String state;

    List<PracticeDto> practices;


    @Bean
    public static ShortChapterDto map(ChapterEntity chapter, boolean hidden, int reportCount) {
        ShortChapterDto dto = new ShortChapterDto();
        dto.id = chapter.getId();
        dto.number = chapter.getNumber();
        dto.shortName = chapter.getShortName();
        dto.reportCount = reportCount;
        dto.hidden = hidden;
        return dto;
    }


    public static ShortChapterDto map(ChapterEntity chapter, boolean hidden) {
        return map(chapter, hidden, 0);
    }


    public static ShortChapterDto map(ChapterEntity chapter, int reportCount) {
        return map(chapter, false, reportCount);
    }


    public static ShortChapterDto map(StudentChapterEntity chapter, int reportCount) {
        ShortChapterDto dto = map(chapter.getChapter(), false, reportCount);
        dto.state = chapter.getState().name();
        dto.practices = chapter.getPractices().stream().map(PracticeDto::map).toList();
        return dto;
    }

}