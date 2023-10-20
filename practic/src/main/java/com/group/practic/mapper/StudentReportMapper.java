package com.group.practic.mapper;

import com.group.practic.dto.StudentReportDto;
import com.group.practic.entity.StudentReportEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudentReportMapper {

    public static final StudentReportMapper INSTANCE = Mappers.getMapper(StudentReportMapper.class);


    @Mapping(target = "personName", expression = "java(report.getStudent().getName())")
    @Mapping(target = "personId", expression = "java(report.getStudent().getId())")
    @Mapping(target = "chapterName", expression = "java(report.getChapter().getShortName())")
    @Mapping(target = "state", expression = "java(report.getState().toString())")
    StudentReportDto map(StudentReportEntity report);


    List<StudentReportDto> map(List<StudentReportEntity> list);

}
