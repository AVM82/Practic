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

    List<ReportDto> reports;


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
        Optional<StudentChapterEntity> chapter = entity.getStudentChapters().stream()
                .filter(studentChapter -> studentChapter.getNumber() == dto.activeChapterNumber)
                .findFirst();
        if (chapter.isPresent()) {
            dto.chapterState = chapter.get().getState().name();
            dto.practices = chapter.get().getPractices().stream().map(PracticeDto::map).toList();
            dto.reports = chapter.get().getReports().stream().map(ReportDto::map).toList();
        }
        return dto;
    }


    public static Optional<StudentDto> map(Optional<StudentEntity> entity) {
        return entity.map(StudentDto::map);
    }

}
