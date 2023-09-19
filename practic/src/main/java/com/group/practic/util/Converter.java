package com.group.practic.util;

import com.group.practic.dto.ChapterDto;
import com.group.practic.dto.CourseDto;
import com.group.practic.dto.PersonApplyOnCourseDto;
import com.group.practic.dto.PersonDto;
import com.group.practic.dto.StudentPracticeDto;
import com.group.practic.dto.StudentReportDto;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonApplicationEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StudentChapterEntity;
import com.group.practic.entity.StudentPracticeEntity;
import com.group.practic.entity.StudentReportEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;


public interface Converter {

    @Bean
    static ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        applyStudentPracticeMap(modelMapper);
        applyStudentReportMap(modelMapper);
        applyPersonOnCourseMap(modelMapper);
        applyStudentChapter(modelMapper);

        return modelMapper;
    }

    static ChapterDto convert(StudentChapterEntity studentChapter) {
        return modelMapper().map(studentChapter, ChapterDto.class);
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


    static StudentReportDto convert(StudentReportEntity studentReportEntity) {
        return modelMapper().map(studentReportEntity, StudentReportDto.class);
    }

    static PersonApplyOnCourseDto convert(PersonApplicationEntity personApplication) {
        return modelMapper().map(personApplication, PersonApplyOnCourseDto.class);
    }


    static List<StudentReportDto> convert(List<StudentReportEntity> studentReportEntityList) {
        if (studentReportEntityList.isEmpty()) {
            return Collections.emptyList();
        }
        List<StudentReportDto> result = new ArrayList<>();
        for (StudentReportEntity reportEntity : studentReportEntityList) {
            result.add(convert(reportEntity));
        }
        return result;
    }


    static List<List<StudentReportDto>> convertListOfLists(
            List<List<StudentReportEntity>> studentReportEntityList) {
        if (studentReportEntityList.isEmpty()) {
            return Collections.emptyList();
        }
        List<List<StudentReportDto>> result = new ArrayList<>();
        for (List<StudentReportEntity> reportEntityList : studentReportEntityList) {
            result.add(convert(reportEntityList));
        }
        return result;

    }


    static List<ChapterDto> convertChapterEntityList(List<ChapterEntity> chapterEntityList) {
        List<ChapterDto> result = new ArrayList<>();
        chapterEntityList.forEach(x -> result.add(convert(x)));
        return result;
    }


    private static void applyStudentPracticeMap(ModelMapper modelMapper) {
        modelMapper.createTypeMap(StudentPracticeEntity.class, StudentPracticeDto.class)
                .addMapping(src -> src.getStudent().getName(), StudentPracticeDto::setPersonName)
                .addMapping(src -> src.getChapterPart().getPraxisPurpose(),
                        StudentPracticeDto::setChapterName)
                .addMapping(StudentPracticeEntity::getState, StudentPracticeDto::setState);
    }


    private static void applyStudentReportMap(ModelMapper modelMapper) {
        modelMapper.createTypeMap(StudentReportEntity.class, StudentReportDto.class)
                .addMapping(src -> src.getStudent().getName(), StudentReportDto::setPersonName)
                .addMapping(src -> src.getStudent().getProfilePictureUrl(),
                        StudentReportDto::setProfilePictureUrl)
                .addMapping(src -> src.getChapter().getShortName(),
                        StudentReportDto::setChapterName)
                .addMapping(StudentReportEntity::getState, StudentReportDto::setState)
                .addMapping(src -> src.getTimeSlot().getDate(), StudentReportDto::setDate)
                .addMapping(src -> src.getTimeSlot().getTime(), StudentReportDto::setTime);
    }

    private static void applyPersonOnCourseMap(ModelMapper modelMapper) {
        modelMapper.createTypeMap(PersonApplicationEntity.class, PersonApplyOnCourseDto.class)
                .addMapping(src -> src.getPerson().getId(), PersonApplyOnCourseDto::setId)
                .addMapping(src -> src.getPerson().getName(), PersonApplyOnCourseDto::setName)
                .addMapping(src -> src.getPerson().getProfilePictureUrl(),
                        PersonApplyOnCourseDto::setProfilePictureUrl)
                .addMapping(src -> src.getCourse().getSlug(),
                        PersonApplyOnCourseDto::setCourseSlug);

    }

    private static void applyStudentChapter(ModelMapper modelMapper) {
        modelMapper.createTypeMap(StudentChapterEntity.class, ChapterDto.class)
                .addMapping(src -> src.getChapter().getId(), ChapterDto::setId)
                .addMapping(src -> src.getChapter().getNumber(), ChapterDto::setNumber)
                .addMapping(src -> src.getChapter().getShortName(), ChapterDto::setShortName);
    }

}
