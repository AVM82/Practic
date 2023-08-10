package com.group.practic.service;

import com.group.practic.dto.CourseDto;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.LevelEntity;
import com.group.practic.repository.CourseRepository;
import com.group.practic.structure.SimpleChapterStructure;
import com.group.practic.util.Converter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

  @Autowired
  private CourseRepository courseRepository;

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

  
  public Optional<CourseEntity> get(String name) {
    return Optional.ofNullable(courseRepository.findByName(name));
  }

  
  public Optional<CourseEntity> create(CourseDto courseDto) {
    return Optional.ofNullable(courseRepository.save(Converter.convert(courseDto)));
  }


  public Optional<CourseEntity> create(CourseEntity course) {
    return Optional.ofNullable(courseRepository.save(course));
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
