package com.group.practic.service;

import com.group.practic.dto.CourseDto;
import com.group.practic.entity.*;
import com.group.practic.repository.CourseRepository;
import com.group.practic.repository.StudentRepository;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.group.practic.structure.SimpleChapterStructure;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    private final ChapterService chapterService;

    private final LevelService levelService;

    public List<CourseEntity> findAll() {
        return courseRepository.findAll();
    }

    public CourseEntity save(CourseDto courseDto) {
        return courseRepository.save(new CourseEntity(courseDto.name(), courseDto.description()));
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

    public List<StudentEntity> findAllStudentsByCourseName(
            String courseName, Boolean inactive, Boolean ban) {
        CourseEntity foundByNameCourseEntity = courseRepository.findByName(courseName);

        if (foundByNameCourseEntity == null) {
            return List.of();
        }

        return foundByNameCourseEntity.getStudents().stream()
                .filter(studentOnCourse ->
                        studentOnCourse.getInactive()
                                .equals(inactive) && studentOnCourse.getBan().equals(ban))
                .map(StudentOnCourse::getStudent)
                .toList();
    }

    public Long create(CourseEntity course) {
        return courseRepository.saveAndFlush(course).getId();
    }


    public Long create(Set<String> authors,
                      String type,
                      String name,
                      String purpose,
                      String description,
                      Map<Integer, List<Integer>> levels,
                      List<SimpleChapterStructure> chapters) {
        if (levels == null || chapters == null) {
            return 0L;
        }
        CourseEntity course = new CourseEntity();
        course.setAuthors(authors.stream().map(AuthorEntity::new).collect(Collectors.toSet()));
        course.setCourseType(type);
        course.setName(name);
        course.setPurpose(purpose);
        course.setDescription(description);
        Long result = courseRepository.saveAndFlush(course).getId();
        List<LevelEntity> levelList = levelService.createMany(course, levels);
        List<ChapterEntity> chapterList = chapterService.createMany(course, chapters);
        return levelList != null && chapterList != null ? result : 0;
    }
}
