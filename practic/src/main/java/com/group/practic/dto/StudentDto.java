package com.group.practic.dto;

import com.group.practic.entity.StudentChapterEntity;
import com.group.practic.entity.StudentEntity;
import java.util.List;
import java.util.Optional;
import lombok.Getter;

@Getter
public class StudentDto {

    long id;

    boolean inactive;

    boolean ban;
    
    String name;
    
    int activeChapterNumber;
    
    String chapterState;
    
    List<PracticeDto> practices;

    List<StudentReportDto> reports;
    
    
    public static StudentDto map(StudentEntity entity) {
        if (entity == null) {
            return null;
        }
        StudentDto dto = new StudentDto();
        dto.id = entity.getId();
        dto.inactive = entity.isInactive();
        dto.ban = entity.isBan();
        dto.name = entity.getPerson().getName();
        dto.activeChapterNumber = entity.getActiveChapterNumber();
        StudentChapterEntity chapter = entity.getStudentChapters().stream().filter(chapter -> chapter.getNumber() == dto.activeChapterNumber).findFirst().get(); 
        dto.chapterState = chapter.getState().name();
        dto.practices = chapter.getPractices().stream().map(PracticeDto::map).toList();
        return dto;
    }


    public static Optional<StudentDto> map(Optional<StudentEntity> entity) {
        return entity.map(StudentDto::map);
    }

}
