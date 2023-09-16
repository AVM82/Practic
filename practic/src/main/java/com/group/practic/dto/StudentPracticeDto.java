package com.group.practic.dto;


import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentPracticeDto {

    private String personName;

    private long chapterPartId;

    private String chapterName;

    private String state;

    private Date updatedAt;
}
