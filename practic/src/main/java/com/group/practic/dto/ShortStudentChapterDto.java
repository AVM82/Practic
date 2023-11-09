package com.group.practic.dto;

import com.group.practic.entity.StudentChapterEntity;
import java.util.List;
import lombok.Getter;


@Getter
public class ShortStudentChapterDto extends ShortChapterDto {

    int reportCount;
          
    String state;
     
    List<PracticeDto> practices;
        

    public static ShortStudentChapterDto map(StudentChapterEntity entity) {
        ShortStudentChapterDto dto = new ShortStudentChapterDto();
        dto.id = entity.getId();
        dto.number = entity.getNumber();
        dto.hidden = false;
        dto.shortName = entity.getChapter().getShortName();
        dto.reportCount = entity.getReportCount();
        dto.state = entity.getState().name();
//        dto.practices = entity.gpractices;
        return dto;
    }

}
