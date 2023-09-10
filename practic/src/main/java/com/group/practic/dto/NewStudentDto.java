package com.group.practic.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class NewStudentDto {

    @NotNull
    @Min(4)
    String courseSlug;

    @NotNull
    long userId;
}
