package com.group.practic.dto;

import com.group.practic.entity.ApplicantEntity;
import lombok.Getter;

@Getter
public class ApplicantDto {

    long id;

    String name;

    String slug;

    String createdAt;
    
    boolean isApplied;
    
    boolean isRejected;
    
    StudentDto student;


    public static ApplicantDto map(ApplicantEntity entity) {
        ApplicantDto dto = new ApplicantDto();
        dto.id = entity.getId();
        dto.name = entity.getPerson().getName();
        dto.slug = entity.getCourse().getSlug();
        dto.createdAt = entity.getCreatedAt().toString();
        dto.isApplied = entity.isApplied();
        dto.isRejected = entity.isRejected();
        dto.student = StudentDto.map(entity.getStudent());
        return dto;
    }

}
