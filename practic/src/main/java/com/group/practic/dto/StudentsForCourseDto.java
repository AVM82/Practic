package com.group.practic.dto;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import java.util.List;
import lombok.Getter;

@Getter
public class StudentsForCourseDto {

    String courseName;
    
    String courseSlug;
    
    List<Integer> chapterNumbers;

    List<StudentDto> students;


    public StudentsForCourseDto(CourseEntity course, List<StudentDto> students) {
        this.courseName = course.getName();
        this.courseSlug = course.getSlug();
        this.chapterNumbers = course.getChapters().stream().map(ChapterEntity::getNumber).toList();
        this.students = students;
    }

}
