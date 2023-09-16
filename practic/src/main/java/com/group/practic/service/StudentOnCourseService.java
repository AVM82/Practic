package com.group.practic.service;

import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonApplicationEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.RoleEntity;
import com.group.practic.entity.StudentOnCourseEntity;
import com.group.practic.repository.PersonApplicationRepository;
import com.group.practic.repository.RoleRepository;
import com.group.practic.repository.StudentOnCourseRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PersonApplicationRepository personApplicationRepository;


    public List<StudentOnCourseEntity> get() {
        return studentOnCourseRepository.findAll();
    }


    public StudentOnCourseEntity get(long id) {
        return studentOnCourseRepository.findByStudentId(id);
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
        Optional<PersonEntity> user = personService.get(studentId);

        Optional<StudentOnCourseEntity> student = (course.isPresent() && user.isPresent())
                ? Optional.of(studentOnCourseRepository
                        .save(new StudentOnCourseEntity(user.get(), course.get())))
                : Optional.empty();

        if (student.isPresent()) {
            PersonEntity updateUser = user.get();
            PersonApplicationEntity applicant =
                    personApplicationRepository.findByPersonAndCourse(updateUser, course.get());
            applicant.setApply(true);
            Set<RoleEntity> roles = updateUser.getRoles();
            roles.add(roleRepository.findByName("STUDENT"));
            roles.add(roleRepository.findByName(course.get().getSlug()));
            personService.save(user.get());
            personApplicationRepository.save(applicant);
        }

        return student;
    }

}
