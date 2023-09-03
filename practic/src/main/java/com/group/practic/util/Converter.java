package com.group.practic.util;

import com.group.practic.dto.ChapterDto;
import com.group.practic.dto.CourseDto;
import com.group.practic.dto.PersonDto;
import com.group.practic.dto.StudentPracticeDto;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StudentPracticeEntity;
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

    
    static ChapterDto convert(ChapterEntity chapterEntity) {
        return modelMapper().map(chapterEntity, ChapterDto.class);
    }
    
    static ChapterEntity convert(ChapterDto chapterDto) {
        return modelMapper().map(chapterDto, ChapterEntity.class);
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

    private static void applyStudentPracticeMap(ModelMapper modelMapper) {
        modelMapper.createTypeMap(StudentPracticeEntity.class, StudentPracticeDto.class)
                .addMapping(src -> src.getStudent().getName(), StudentPracticeDto::setPersonName)
                .addMapping(src ->
                        src.getChapter().getShortName(), StudentPracticeDto::setChapterName)
                .addMapping(StudentPracticeEntity::getState, StudentPracticeDto::setState);
    }
}
