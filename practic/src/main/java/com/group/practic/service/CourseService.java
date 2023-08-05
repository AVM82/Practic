package com.group.practic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.group.practic.dto.CourseDTO;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.StudentEntity;
import com.group.practic.entity.StudentOnCourse;
import com.group.practic.repository.CourseRepository;
import com.group.practic.repository.StudentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public List<CourseEntity> findAll() {
        return courseRepository.findAll();
    }

    public CourseEntity save(CourseDTO courseDTO) {
        return courseRepository.save(new CourseEntity(courseDTO.name(), courseDTO.description()));
    }

    public CourseEntity addStudentToCourse(String courseName, String studentPib) {
        CourseEntity courseEntity = courseRepository.findByName(courseName);
        StudentEntity studentEntity = studentRepository.findByPib(studentPib);

        if (courseEntity != null && studentEntity != null) {
            StudentOnCourse studentOnCourse = new StudentOnCourse();
            studentOnCourse.setStudent(studentEntity);
            studentOnCourse.setCourse(courseEntity);

            courseEntity.getStudents().add(studentOnCourse);

            return courseRepository.save(courseEntity);
        }

        return null;
    }

    public List<StudentEntity> findAllStudentsByCourseName(String courseName, Boolean inactive, Boolean ban) {
        CourseEntity foundByNameCourseEntity = courseRepository.findByName(courseName);

        if (foundByNameCourseEntity == null) {
            return List.of();
        }

        return foundByNameCourseEntity.getStudents().stream()
                .filter(studentOnCourse ->
                        studentOnCourse.getInactive().equals(inactive) && studentOnCourse.getBan().equals(ban))
                .map(StudentOnCourse::getStudent)
                .toList();
    }
}
