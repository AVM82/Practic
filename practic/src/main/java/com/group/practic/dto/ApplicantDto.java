package com.group.practic.dto;

import com.group.practic.entity.ApplicantEntity;
import lombok.Getter;

@Getter
public class ApplicantDto {

    long id;

    String name;
    
    String createdAt;
    
    boolean isApplied;
    
    boolean isRejected;
    
    PersonStudentDto student;


    public static ApplicantDto map(ApplicantEntity entity) {
        ApplicantDto dto = new ApplicantDto();
        dto.id = entity.getId();
        dto.name = entity.getPerson().getName();
        dto.createdAt = entity.getCreatedAt().toString();
        dto.isApplied = entity.isApplied();
        dto.isRejected = entity.isRejected();
        dto.student = PersonStudentDto.map(entity.getStudent());
        return dto;
    }

}
