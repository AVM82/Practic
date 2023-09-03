package com.group.practic.service;

import com.group.practic.dto.CourseDto;
import com.group.practic.entity.AdditionalMaterialsEntity;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.LevelEntity;
import com.group.practic.repository.CourseRepository;
import com.group.practic.util.Converter;
import jakarta.validation.constraints.Min;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CourseService {

    CourseRepository courseRepository;

    ChapterService chapterService;

    LevelService levelService;


    @Autowired
    public CourseService(CourseRepository courseRepository, ChapterService chapterService,
            LevelService levelService) {
        super();
        this.courseRepository = courseRepository;
        this.chapterService = chapterService;
        this.levelService = levelService;
    }


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
        return course.isEmpty() ? List.of() : chapterService.getAll(course.get());
    }


    public List<LevelEntity> getLevels(@Min(1) long id) {
        Optional<CourseEntity> course = courseRepository.findById(id);
        return course.isEmpty() ? List.of() : levelService.getAll(course.get());
    }


    public Optional<ChapterEntity> getChapterByNumber(String slug, int number) {
        Optional<CourseEntity> course = get(slug);
        return course.isEmpty() ? Optional.empty()
                : chapterService.getChapterByNumber(course.get(), number);
    }


    public Optional<String> getPurpose(long id) {
        Optional<CourseEntity> course = courseRepository.findById(id);
        return course.isEmpty() ? Optional.empty() : Optional.ofNullable(course.get().getPurpose());
    }


    public Optional<String> getDescription(long id) {
        Optional<CourseEntity> course = courseRepository.findById(id);
        return course.isEmpty() ? Optional.empty()
                : Optional.ofNullable(course.get().getDescription());
    }


    public Set<AdditionalMaterialsEntity> getAdditional(String slug) {
        Optional<CourseEntity> course = get(slug);
        return course.isEmpty() ? null : course.get().getAdditionalMaterials();
    }


    public Optional<CourseEntity> getByShortName(String shortName) {
        return courseRepository.findByShortName(shortName);
    }


    public Optional<CourseEntity> create(CourseDto courseDto) {
        return Optional.ofNullable(courseRepository.save(Converter.convert(courseDto)));
    }


    public Optional<CourseEntity> save(CourseEntity course) {
        return Optional.ofNullable(courseRepository.save(course));
    }


    public Optional<CourseEntity> addShortName(long id, String shortName) {
        Optional<CourseEntity> course = courseRepository.findById(id);
        CourseEntity courseEntity;
        if (course.isPresent()) {
            courseEntity = course.get();
            courseEntity.setShortName(shortName);
            return Optional.of(courseRepository.save(courseEntity));
        }
        return Optional.empty();
    }

}
