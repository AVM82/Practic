package com.group.practic.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class StudentChapterDto {

    @NotNull
    @Min(1)
    long studentId;

    @NotNull
    @Min(1)
    long chapterId;
}
