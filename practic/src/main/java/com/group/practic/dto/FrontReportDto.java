package com.group.practic.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FrontReportDto {

    long studentChapterId;
    
    long personId;
    
    LocalDate date;
    
    int chapterNumber;
    
    long topicReportId;
    
}
