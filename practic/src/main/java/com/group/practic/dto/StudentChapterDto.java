package com.group.practic.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentChapterDto {

    @Min(1)
    long studentId;

    @Min(1)
    long chapterId;

}
