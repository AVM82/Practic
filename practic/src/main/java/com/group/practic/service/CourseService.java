package com.group.practic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.group.practic.dto.CourseDTO;
import com.group.practic.entity.Course;
import com.group.practic.entity.Student;
import com.group.practic.entity.StudentOnCourse;
import com.group.practic.repository.CourseRepository;
import com.group.practic.repository.StudentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public Course save(CourseDTO courseDTO) {
        return courseRepository.save(new Course(courseDTO.name(), courseDTO.description()));
    }

    public Course addStudentToCourse(String courseName, String studentPib) {
        Course course = courseRepository.findByName(courseName);
        Student student = studentRepository.findByPib(studentPib);

        if (course != null && student != null) {
            StudentOnCourse studentOnCourse = new StudentOnCourse();
            studentOnCourse.setStudent(student);
            studentOnCourse.setCourse(course);

            course.getStudents().add(studentOnCourse);

            return courseRepository.save(course);
        }

        return null;
    }

    public List<Student> findAllStudentsByCourseName(String courseName) {
        Course foundByNameCourse = courseRepository.findByName(courseName);

        if (foundByNameCourse == null) {
            return List.of();
        }

        return foundByNameCourse.getStudents().stream()
                .filter(studentOnCourse -> !studentOnCourse.getInactive())
                .map(StudentOnCourse::getStudent)
                .toList();
    }
}
