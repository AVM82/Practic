package com.group.practic.service;

import com.group.practic.dto.CourseDto;
import com.group.practic.entity.AuthorEntity;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.LevelEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StudentOnCourse;
import com.group.practic.repository.CourseRepository;
import com.group.practic.structure.SimpleChapterStructure;
import com.group.practic.util.Converter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private PersonService personService;

  @Autowired
  private ChapterService chapterService;

  @Autowired
  private LevelService levelService;


  public List<CourseEntity> get() {
    return courseRepository.findAll();
  }

  
  public Optional<CourseEntity> get(long id) {
    return courseRepository.findById(id);
  }

  
  public CourseEntity create(CourseDto courseDto) {
    return courseRepository.save(Converter.convert(courseDto));
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

  
  public List<StudentEntity> findAllStudentsByCourseName(String courseName, Boolean inactive,
      Boolean ban) {
    CourseEntity foundByNameCourseEntity = courseRepository.findByName(courseName);

    if (foundByNameCourseEntity == null) {
      return List.of();
    }

    return foundByNameCourseEntity.getStudents().stream()
        .filter(studentOnCourse -> studentOnCourse.getInactive().equals(inactive)
            && studentOnCourse.getBan().equals(ban))
        .map(StudentOnCourse::getStudent).toList();
  }

  
  public long create(CourseEntity course) {
    return courseRepository.saveAndFlush(course).getId();
  }


  public long create(Set<String> authors, String type, String name, String purpose,
      String description, Map<Integer, List<Integer>> levels,
      List<SimpleChapterStructure> chapters) {
    if (levels == null || chapters == null) {
      return 0L;
    }
    CourseEntity course = new CourseEntity();
    course.setAuthors(authors);
    course.setCourseType(type);
    course.setName(name);
    course.setPurpose(purpose);
    course.setDescription(description);
    Long result = courseRepository.saveAndFlush(course).getId();
    List<LevelEntity> levelList = levelService.createMany(course, levels);
    List<ChapterEntity> chapterList = chapterService.createMany(course, chapters);
    return (levelList != null && chapterList != null) ? result : 0;
  }

}
