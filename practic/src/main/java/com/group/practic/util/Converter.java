package com.group.practic.util;

import com.group.practic.dto.CourseDto;
import com.group.practic.dto.PersonDto;
import com.group.practic.dto.StudentReportDto;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StudentReportEntity;
import java.util.List;
import java.util.Set;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;

public interface Converter {

    @Bean
    static ModelMapper modelMapper() {
        return new ModelMapper();
    }


    public static <T> List<T> nonNullList(List<T> list) {
        return list == null ? List.of() : list;
    }


    public static <T> List<T> nonNullList(Set<T> set) {
        return set == null ? List.of() : set.stream().toList();
    }


    static CourseEntity convert(CourseDto courseDto) {
        return modelMapper().map(courseDto, CourseEntity.class);
    }


    static PersonDto convert(PersonEntity personEntity) {
        return PersonDto.map(personEntity);
    }


    static PersonEntity convert(PersonDto personDto) {
        return modelMapper().map(personDto, PersonEntity.class);
    }


    static StudentReportDto convert(StudentReportEntity studentReportEntity) {
        return StudentReportDto.map(studentReportEntity);
    }


    static List<StudentReportDto> convert(List<StudentReportEntity> studentReportEntityList) {
        return studentReportEntityList.stream().map(StudentReportDto::map).toList();
    }


    static List<List<StudentReportDto>> convertListOfLists(
            List<List<StudentReportEntity>> studentReportEntityList) {
        return studentReportEntityList.stream().map(Converter::convert).toList();
    }
}
 
