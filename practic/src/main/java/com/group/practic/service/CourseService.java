package com.group.practic.service;

import com.group.practic.PropertyLoader;
import com.group.practic.dto.MentorDto;
import com.group.practic.dto.NewCourseDto;
import com.group.practic.dto.ShortChapterDto;
import com.group.practic.entity.AdditionalMaterialsEntity;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.LevelEntity;
import com.group.practic.repository.CourseRepository;
import com.group.practic.util.PropertyUtil;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CourseService {

    CourseRepository courseRepository;

    ChapterService chapterService;

    LevelService levelService;

    AdditionalMaterialsService additionalMaterialsService;

    ReferenceTitleService referenceTitleService;


    @Autowired
    public CourseService(CourseRepository courseRepository, ChapterService chapterService,
            LevelService levelService, AdditionalMaterialsService additionalMaterialsService,
            ReferenceTitleService referenceTitleService) {
        this.courseRepository = courseRepository;
        this.chapterService = chapterService;
        this.levelService = levelService;
        this.additionalMaterialsService = additionalMaterialsService;
        this.referenceTitleService = referenceTitleService;
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


    public List<ShortChapterDto> getChapters(CourseEntity course, boolean hide) {
        return Optional.ofNullable(course.getChapters())
                .map(chapters -> chapters.stream()
                        .map(chapter -> ShortChapterDto.map(chapter, hide)).toList())
                .orElse(List.of());
    }


    public List<LevelEntity> getLevels(String slug) {
        Optional<CourseEntity> course = courseRepository.findBySlug(slug);
        return course.isEmpty() ? List.of() : levelService.getAll(course.get());
    }


    public Optional<ChapterEntity> getChapterByNumber(CourseEntity course, int number) {
        return chapterService.get(course, number);
    }


    public List<AdditionalMaterialsEntity> getAdditional(String slug) {
        Optional<CourseEntity> course = get(slug);
        return course.isEmpty() ? null : course.get().getAdditionalMaterials();
    }


    public Optional<CourseEntity> save(CourseEntity course) {
        return Optional.ofNullable(courseRepository.save(course));
    }


    public Optional<CourseEntity> create(NewCourseDto dto) {
        Optional<CourseEntity> exists = get(dto.getSlug());
        if (exists.isEmpty()) {
            CourseEntity course = new CourseEntity();
            course.setName(dto.getName());
            course.setSlug(dto.getSlug());
            course.setSvg(dto.getSvg());
            return Optional.ofNullable(courseRepository.save(course));
        }
        return Optional.empty();
    }


    public Optional<CourseEntity> create(String properties) {
        PropertyLoader prop = new PropertyLoader(properties, true);
        return prop.initialized ? createOrUpdate(prop) : Optional.empty();
    }


    public Optional<CourseEntity> createOrUpdate(PropertyLoader prop) {
        String slug = prop.getProperty(PropertyUtil.SLUG_KEY, "");
        if (slug.length() < 5) {
            return Optional.empty();
        }
        CourseEntity courseEntity =
                new CourseEntity(slug, prop.getProperty(PropertyUtil.NAME_KEY, ""),
                        prop.getProperty(PropertyUtil.SVG_KEY, ""));
        courseEntity.setAuthors(getAuthorSet(prop));
        courseEntity.setCourseType(prop.getProperty(PropertyUtil.TYPE_KEY, ""));
        courseEntity.setDescription(prop.getProperty(PropertyUtil.DESCRIPTION_KEY, ""));
        Optional<CourseEntity> course = get(slug);
        courseEntity = course.isEmpty() ? courseRepository.save(courseEntity)
                : course.get().update(courseEntity);
        courseEntity.setLevels(levelService.getLevelsSet(courseEntity, prop));
        courseEntity.setChapters(chapterService.getChapters(courseEntity, prop));
        courseEntity.setAdditionalMaterials(
                additionalMaterialsService.getAdditionalMaterials(courseEntity, prop));
        return save(courseEntity);
    }


    protected Set<String> getAuthorSet(PropertyLoader prop) {
        Set<String> result = new HashSet<>();
        for (Entry<Object, Object> entry : prop.getEntrySet()) {
            if (PropertyUtil.keyStartsWith(entry.getKey(), PropertyUtil.AUTHOR_KEY)) {
                result.add((String) entry.getValue());
            }
        }
        return result;
    }


    public List<MentorDto> getMentors(CourseEntity course) {
        return course.getMentors().stream().map(MentorDto::map).toList();
    }


    public Optional<ChapterEntity> getNextChapterByNumber(CourseEntity course, int number) {
        return course.getChapters().stream().sequential()
                .filter(chapter -> chapter.getNumber() > number).findFirst();
    }

}

