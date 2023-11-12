package com.group.practic.dto;

import com.group.practic.entity.StudentPracticeEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentPracticeDto {

    @Min(1)
    long id;

    @Min(1)
    long chapterPartId;

    @NotEmpty
    String state;


    public static StudentPracticeDto map(StudentPracticeEntity entity) {
        if (entity == null) {
            return null;
        }
        StudentPracticeDto dto = new StudentPracticeDto();
        dto.id = entity.getId();
        dto.chapterPartId = entity.getChapterPartId();
        dto.state = entity.getState().name();
        return dto;
    }


    public static Optional<StudentPracticeDto> map(Optional<StudentPracticeEntity> entity) {
        return entity.isPresent() ? Optional.ofNullable(map(entity.get())) : Optional.empty();
    }

}
