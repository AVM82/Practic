package com.group.practic.service;

import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StudentOnCourseEntity;
import com.group.practic.repository.StudentOnCourseRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StudentOnCourseService {

    @Autowired
    StudentOnCourseRepository studentOnCourseRepository;

    @Autowired
    CourseService courseService;

    @Autowired
    PersonService personService;


    public List<StudentOnCourseEntity> get() {
        return studentOnCourseRepository.findAll();
    }


    public Optional<StudentOnCourseEntity> get(long id) {
        return studentOnCourseRepository.findById(id);
    }


    public List<StudentOnCourseEntity> get(boolean inactive, boolean ban) {
        return studentOnCourseRepository.findAllByInactiveAndBan(inactive, ban);
    }


    public List<StudentOnCourseEntity> get(long courseId, long studentId, boolean inactive,
            boolean ban) {
        Optional<CourseEntity> course = courseService.get(courseId);
        Optional<PersonEntity> student = personService.get(studentId);
        return (course.isPresent() && student.isPresent())
                ? studentOnCourseRepository.findAllByCourseAndStudentAndInactiveAndBan(
                        course.get(), student.get(), inactive, ban)
                : List.of();
    }


    public List<StudentOnCourseEntity> getCoursesOfStudent(long studentId, boolean inactive,
            boolean ban) {
        Optional<PersonEntity> student = personService.get(studentId);
        return student.isPresent()
                ? studentOnCourseRepository.findAllByStudentAndInactiveAndBan(student.get(),
                        inactive, ban)
                : List.of();
    }


    public List<StudentOnCourseEntity> getStudentsOfCourse(long courseId, boolean inactive,
            boolean ban) {
        Optional<CourseEntity> course = courseService.get(courseId);
        return course.isPresent()
                ? studentOnCourseRepository.findAllByCourseAndInactiveAndBan(course.get(), inactive,
                        ban)
                : List.of();
    }


    public Optional<StudentOnCourseEntity> create(long courseId, long studentId) {
        Optional<CourseEntity> course = courseService.get(courseId);
        Optional<PersonEntity> student = personService.get(studentId);
        return (course.isPresent() && student.isPresent())
                ? Optional.ofNullable(studentOnCourseRepository
                        .save(new StudentOnCourseEntity(student.get(), course.get())))
                : Optional.empty();
    }

}
