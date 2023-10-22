package com.group.practic.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PracticeDto {

    @NotNull
    private long chapterPartId;

    @NotNull
    private String state;
}
