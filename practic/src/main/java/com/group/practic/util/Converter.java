package com.group.practic.util;

import com.group.practic.dto.CourseDto;
import com.group.practic.dto.PersonDto;
import com.group.practic.dto.StudentPracticeDto;
import com.group.practic.dto.StudentReportDto;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StudentPracticeEntity;
import com.group.practic.entity.StudentReportEntity;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;


public interface Converter {

    @Bean
    static ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        applyStudentPracticeMap(modelMapper);

        return modelMapper;
    }

    static CourseDto convert(CourseEntity courseEntity) {
        return modelMapper().map(courseEntity, CourseDto.class);
    }


    static CourseEntity convert(CourseDto courseDto) {
        return modelMapper().map(courseDto, CourseEntity.class);
    }


    static PersonDto convert(PersonEntity personEntity) {
        return modelMapper().map(personEntity, PersonDto.class);
    }


    static PersonEntity convert(PersonDto personDto) {
        return modelMapper().map(personDto, PersonEntity.class);
    }

    static StudentPracticeDto convert(StudentPracticeEntity studentPracticeEntity) {
        return modelMapper().map(studentPracticeEntity, StudentPracticeDto.class);
    }
    static StudentReportDto convert(StudentReportEntity studentReportEntity) {
        return modelMapper().map(studentReportEntity, StudentReportDto.class);
    }

    private static void applyStudentPracticeMap(ModelMapper modelMapper) {
        modelMapper.createTypeMap(StudentPracticeEntity.class, StudentPracticeDto.class)
                .addMapping(src -> src.getStudent().getName(), StudentPracticeDto::setPersonName)
                .addMapping(src ->
                        src.getChapter().getShortName(), StudentPracticeDto::setChapterName)
                .addMapping(StudentPracticeEntity::getState, StudentPracticeDto::setState);
    }
}
