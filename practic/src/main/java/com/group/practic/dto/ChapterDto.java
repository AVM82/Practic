package com.group.practic.dto;

import com.group.practic.entity.ChapterEntity;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import org.springframework.context.annotation.Bean;

@Getter
public class ChapterDto {

    long id;

    int number;

    String shortName;

    boolean visible;

    Set<Long> partsId = new HashSet<>();


    public void setVisible(boolean value) {
        this.visible = value;
    }

    @Bean
    public static ChapterDto map(ChapterEntity chapter, boolean visible) {
        ChapterDto dto = new ChapterDto();
        dto.id = chapter.getId();
        dto.number = chapter.getNumber();
        dto.shortName = chapter.getShortName();
        dto.visible = visible;
        chapter.getParts().forEach(part -> dto.partsId.add(part.getId()));
        return dto;
    }


    public static ChapterDto map(ChapterEntity chapter) {
        return map(chapter, true);
    }

}
