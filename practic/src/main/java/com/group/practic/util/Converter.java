package com.group.practic.util;

import com.group.practic.dto.AnswerDto;
import com.group.practic.dto.ChapterDto;
import com.group.practic.dto.CourseDto;
import com.group.practic.dto.PersonApplyOnCourseDto;
import com.group.practic.dto.PersonDto;
import com.group.practic.dto.PracticeDto;
import com.group.practic.dto.QuestionDto;
import com.group.practic.dto.QuizDto;
import com.group.practic.dto.StudentPracticeDto;
import com.group.practic.dto.StudentReportDto;
import com.group.practic.entity.AnswerEntity;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonApplicationEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.QuestionEntity;
import com.group.practic.entity.QuizEntity;
import com.group.practic.entity.StudentChapterEntity;
import com.group.practic.entity.StudentPracticeEntity;
import com.group.practic.entity.StudentReportEntity;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;

public interface Converter {

    @Bean
    static ModelMapper modelMapper() {
        return new ModelMapper();
    }


    static ChapterDto convert(StudentChapterEntity studentChapter) {
        return ChapterDto.map(studentChapter.getChapter());
    }


    static CourseEntity convert(CourseDto courseDto) {
        return modelMapper().map(courseDto, CourseEntity.class);
    }


    static ChapterEntity convert(ChapterDto chapterDto) {
        return modelMapper().map(chapterDto, ChapterEntity.class);
    }


    static PersonDto convert(PersonEntity personEntity) {
        return PersonDto.map(personEntity);
    }


    static PersonEntity convert(PersonDto personDto) {
        return modelMapper().map(personDto, PersonEntity.class);
    }


    static StudentPracticeDto convert(StudentPracticeEntity studentPracticeEntity) {
        return StudentPracticeDto.map(studentPracticeEntity);
    }


    static StudentReportDto convert(StudentReportEntity studentReportEntity) {
        return StudentReportDto.map(studentReportEntity);
    }


    static PersonApplyOnCourseDto convert(PersonApplicationEntity personApplication) {
        return PersonApplyOnCourseDto.map(personApplication);
    }


    static List<StudentReportDto> convert(List<StudentReportEntity> studentReportEntityList) {
        return studentReportEntityList.stream().map(StudentReportDto::map).toList();
    }


    static List<CourseDto> convertCourseEntityList(List<CourseEntity> courses) {
        List<CourseDto> result = new ArrayList<>(courses.size());
        courses.forEach(course -> {
            if (!course.getInactive()) {
                result.add(CourseDto.map(course));
            }
        });
        return result;
    }


    static PracticeDto convertToPractice(StudentPracticeEntity studentPracticeEntity) {
        return new PracticeDto(studentPracticeEntity.getChapterPart().getId(),
                studentPracticeEntity.getState().name());
    }


    static List<List<StudentReportDto>> convertListOfLists(
            List<List<StudentReportEntity>> studentReportEntityList) {
        return studentReportEntityList.stream().map(Converter::convert).toList();
    }


    static List<ChapterDto> convertChapterList(List<ChapterEntity> chapterList, int lastNumber) {
        List<ChapterDto> result = new ArrayList<>();
        chapterList.forEach(x -> result.add(ChapterDto.map(x, lastNumber >= x.getNumber())));
        return result;
    }


    static AnswerDto toDto(AnswerEntity entity) {
        return new AnswerDto(entity.getId(), entity.getAnswer(), false);
    }


    static QuestionDto toDto(QuestionEntity entity) {
        QuestionDto dto = new QuestionDto();
        dto.setId(entity.getId());
        dto.setQuestion(entity.getQuestion());
        List<AnswerDto> answerDtos = entity.getAnswers().stream().map(Converter::toDto).toList();
        dto.setAnswers(answerDtos);
        return dto;
    }


    static QuizDto toDto(QuizEntity entity) {
        QuizDto dto = new QuizDto();
        dto.setId(entity.getId());
        dto.setQuizName(entity.getChapterName());
        List<QuestionDto> questionDtos =
                entity.getQuestions().stream().map(Converter::toDto).toList();
        dto.setQuestions(questionDtos);
        return dto;
    }

}
