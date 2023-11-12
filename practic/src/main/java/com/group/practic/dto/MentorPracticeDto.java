package com.group.practic.dto;

import com.group.practic.entity.StudentPracticeEntity;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class MentorPracticeDto {

    @Min(1)
    private long studentId;

    private String personName;

    private long chapterPartId;

    private String purpose;

    private String state;

    private LocalDateTime updatedAt;



    public static MentorPracticeDto map(StudentPracticeEntity practice) {
        MentorPracticeDto dto = new MentorPracticeDto();
//        dto.studentId = practice.getStudent().getId();
//        dto.personName = practice.getStudent().getPerson().getName();
        dto.chapterPartId = practice.getChapterPartId();
//        dto.purpose = practice.getChapterPart().getPraxisPurpose();
        dto.state = practice.getState().name();
        dto.updatedAt = practice.getUpdatedAt();
        return dto;
    }

}
