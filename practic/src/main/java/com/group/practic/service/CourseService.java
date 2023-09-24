package com.group.practic.service;

import com.group.practic.PropertyLoader;
import com.group.practic.dto.ChapterDto;
import com.group.practic.dto.CourseDto;
import com.group.practic.entity.AdditionalMaterialsEntity;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.LevelEntity;
import com.group.practic.repository.CourseRepository;
import com.group.practic.util.Converter;
import com.group.practic.util.PropertyUtil;
import jakarta.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CourseService {

    CourseRepository courseRepository;

    ChapterService chapterService;

    LevelService levelService;

    AdditionalMaterialsService additionalMaterialsService;


    @Autowired
    public CourseService(CourseRepository courseRepository, ChapterService chapterService,
            LevelService levelService, AdditionalMaterialsService additionalMaterialsService) {
        this.courseRepository = courseRepository;
        this.chapterService = chapterService;
        this.levelService = levelService;
        this.additionalMaterialsService = additionalMaterialsService;
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


    public List<ChapterDto> getChapters(String slug) {
        Optional<CourseEntity> course = courseRepository.findBySlug(slug);
        return course.isEmpty() ? List.of()
                : Converter.convertChapterEntityList(chapterService.getAll(course.get()));
    }


    public List<LevelEntity> getLevels(String slug) {
        Optional<CourseEntity> course = courseRepository.findBySlug(slug);
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


    public Optional<CourseEntity> changeShortName(long id, String shortName) {
        Optional<CourseEntity> course = get(id);
        if (course.isPresent()) {
            course.get().setShortName(shortName);
        }
        return course;
    }


    public Optional<CourseEntity> save(CourseEntity course) {
        return Optional.ofNullable(courseRepository.save(course));
    }


    public Optional<CourseEntity> create(CourseDto courseDto) {
        return Optional.ofNullable(courseRepository.save(Converter.convert(courseDto)));
    }


    public Optional<CourseEntity> create(String properties) {
        PropertyLoader prop = new PropertyLoader(properties, true);
        return prop.initialized ? create(prop) : Optional.empty();
    }


    public Optional<CourseEntity> create(PropertyLoader prop) {
        CourseEntity courseEntity;
        String slug = prop.getProperty(PropertyUtil.SLUG_KEY, "");
        Optional<CourseEntity> course = get(slug);
        String shortName = prop.getProperty(PropertyUtil.SHORT_NAME_KEY, "");
        String name = prop.getProperty(PropertyUtil.NAME_KEY, "");
        String svg = prop.getProperty(PropertyUtil.SVG_KEY, "");
        if (course.isPresent()) {
            courseEntity = course.get();
            courseEntity.setShortName(shortName);
            courseEntity.setName(name);
            courseEntity.setSvg(svg);
        } else {
            course = save(new CourseEntity(slug, shortName, name, svg));
            if (course.isEmpty()) {
                return Optional.empty();
            }
            courseEntity = course.get();
        }
        courseEntity.setAuthors(getAuthorSet(prop));
        courseEntity.setCourseType(prop.getProperty(PropertyUtil.TYPE_KEY, ""));
        courseEntity.setPurpose(prop.getProperty(PropertyUtil.PURPOSE_KEY, ""));
        courseEntity.setDescription(prop.getProperty(PropertyUtil.DESCRIPTION_KEY, ""));
        levelService.getLevelsSet(courseEntity, prop);
        chapterService.getChapters(courseEntity, prop);
        additionalMaterialsService.getAdditionalMaterials(courseEntity, prop);
        return save(courseEntity);
    }


    protected String getAuthorSet(PropertyLoader prop) {
        List<String> result = new ArrayList<>();
        for (Entry<Object, Object> entry : prop.getEntrySet()) {
            if (PropertyUtil.keyStartsWith(entry.getKey(), PropertyUtil.AUTHOR_KEY)) {
                result.add((String) entry.getValue());
            }
        }
        return result.stream().collect(Collectors.joining(PropertyUtil.AUTHOR_SEPARATOR));
    }

}
