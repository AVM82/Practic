package com.group.practic.dto;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.StudentChapterEntity;
import lombok.Getter;
import org.springframework.context.annotation.Bean;

@Getter
public class ShortChapterDto {

    long id;

    int number;

    String shortName;

    boolean hidden;


    @Bean
    public static ShortChapterDto map(ChapterEntity chapter, boolean hidden) {
        ShortChapterDto dto = new ShortChapterDto();
        dto.id = chapter.getId();
        dto.number = chapter.getNumber();
        dto.shortName = chapter.getShortName();
        dto.hidden = hidden;
        return dto;
    }


    public static ShortChapterDto map(StudentChapterEntity chapter) {
        return map(chapter.getChapter(), false);
    }

}
