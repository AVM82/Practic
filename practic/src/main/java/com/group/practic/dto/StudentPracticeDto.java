package com.group.practic.dto;


import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentPracticeDto {

    private String personName;

    @NotNull
    private long chapterPartId;

    private String chapterName;

    private String state;

    private Date updatedAt;

    @NotNull
    private long studentId;
}
