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


    public Optional<CourseEntity> get(String slug) {
        return courseRepository.findBySlug(slug);
    }


    public List<ChapterEntity> getChapters(long id) {
        Optional<CourseEntity> course = courseRepository.findById(id);
        return course.isEmpty() ? List.of() : course.get().getChapters();
    }


    public Optional<String> getPurpose(long id) {
        Optional<CourseEntity> course = courseRepository.findById(id);
        return course.isEmpty() ? Optional.empty()
                : Optional.ofNullable(course.get().getPurpose());
    }


    public Optional<String> getDescription(long id) {
        Optional<CourseEntity> course = courseRepository.findById(id);
        return course.isEmpty() ? Optional.empty() : 
            Optional.ofNullable(course.get().getDescription());
    }


    public Optional<ChapterEntity> getAdditional(long id) {
        Optional<CourseEntity> course = courseRepository.findById(id);
        return course.isEmpty() ? Optional.empty() 
                : Optional.ofNullable(course.get().getAdditionalMaterials());
    }


    public Optional<CourseEntity> create(CourseDto courseDto) {
        return Optional.ofNullable(courseRepository.save(Converter.convert(courseDto)));
    }


    public Optional<CourseEntity> create(CourseEntity course) {
        return Optional.ofNullable(courseRepository.save(course));
    }


    public long create(String authors, String type, String shortname, String name, String purpose, String description,
            Map<Integer, List<Integer>> levels, List<SimpleChapterStructure> chapters, String slug) {
        if (levels == null || chapters == null) {
            return 0L;
        }
        CourseEntity course = new CourseEntity();
        course.setAuthors(authors);
        course.setCourseType(type);
        course.setShortName(shortname);
        course.setName(name);
        course.setPurpose(purpose);
        course.setDescription(description);
        course.setSlug(slug);
        Long result = courseRepository.saveAndFlush(course).getId();
        List<LevelEntity> levelList = levelService.createMany(course, levels);
        List<ChapterEntity> chapterList = chapterService.createMany(course, chapters);
        return (levelList != null && chapterList != null) ? result : 0;
    }

}
