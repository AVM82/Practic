package com.group.practic;

import com.group.practic.entity.AdditionalEntity;
import com.group.practic.entity.AdditionalMaterialsEntity;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.ChapterPartEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.LevelEntity;
import com.group.practic.entity.PraxisEntity;
import com.group.practic.entity.ReferenceTitleEntity;
import com.group.practic.entity.SubChapterEntity;
import com.group.practic.entity.SubSubChapterEntity;
import com.group.practic.service.AdditionalMaterialsService;
import com.group.practic.service.ChapterPartService;
import com.group.practic.service.ChapterService;
import com.group.practic.service.CourseService;
import com.group.practic.service.LevelService;
import com.group.practic.service.ReferenceTitleService;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class CoursesInitializator {

    public static final String COURSE_MASK = ".course";

    public static final String NAME_KEY = "name";

    public static final String SHORT_NAME_KEY = "shortname";

    public static final String AUTHOR_KEY = "author";

    public static final String TYPE_KEY = "type";

    public static final String DESCRIPTION_KEY = "description";

    public static final String PURPOSE_KEY = "purpose";

    public static final String LEVEL_KEY = "level";

    public static final String SLUG_KEY = "slug";

    private static final int LEVEL_KEY_LENGTH = LEVEL_KEY.length();

    public static final String SEPARATOR = File.separator;

    public static final String NAME_SEPARATOR = "<>";

    public static final String REFERENCE_SEPARATOR = " ";

    public static final String AUTHOR_SEPARATOR = ", ";

    public static final String PART_SEPARATOR = "-";

    public static final String PRAXIS_PART = "prac.";

    public static final String ADDITIONAL_PART = "add.";

    public static final char DOT = '.';

    CourseService courseService;

    ChapterService chapterService;

    ChapterPartService chapterPartService;

    LevelService levelService;

    ReferenceTitleService referenceTitleService;

    AdditionalMaterialsService additionalMaterialsService;

    @Value("${refreshDB_on_start}")
    boolean refresh;


    @Autowired
    CoursesInitializator(CourseService courseService, LevelService levelService,
            ChapterService chapterService, ReferenceTitleService referenceTitleService,
            AdditionalMaterialsService additionalMaterialsService,
            ChapterPartService chapterPartService) {
        this.courseService = courseService;
        this.levelService = levelService;
        this.chapterService = chapterService;
        this.chapterPartService = chapterPartService;
        this.referenceTitleService = referenceTitleService;
        this.additionalMaterialsService = additionalMaterialsService;
    }


    @PostConstruct
    void initialize() {
        if (refresh) {
            File[] files = new File(".")
                    .listFiles(f -> f.isFile() && f.getName().endsWith(COURSE_MASK));
            for (File file : files) {
                initCourse(file.getName());
            }
        }
    }


    public boolean initCourse(String filename) {
        if (PropertyLoader.isComprehendedString(filename)) {
            PropertyLoader prop = new PropertyLoader(filename);
            if (prop.initialized) {
                CourseEntity courseEntity;
                String slug = prop.getProperty(SLUG_KEY, "");
                Optional<CourseEntity> course = courseService.get(slug);
                if (course.isPresent()) {
                    courseEntity = course.get();
                    courseEntity.setShortName(prop.getProperty(SHORT_NAME_KEY, ""));
                    courseEntity.setName(prop.getProperty(NAME_KEY, ""));
                } else {
                    course = courseService.save(new CourseEntity(slug,
                            prop.getProperty(SHORT_NAME_KEY, ""), prop.getProperty(NAME_KEY, "")));
                    if (course.isEmpty()) {
                        return false;
                    }
                    courseEntity = course.get();
                }
                courseEntity.setAuthors(getAuthorSet(prop));
                courseEntity.setCourseType(prop.getProperty(TYPE_KEY, ""));
                courseEntity.setPurpose(prop.getProperty(PURPOSE_KEY, ""));
                courseEntity.setDescription(prop.getProperty(DESCRIPTION_KEY, ""));
                getLevelsSet(courseEntity, prop);
                getChapters(courseEntity, prop);
                courseEntity.setAdditionalMaterials(getAdditionalMaterials(prop));
                course = courseService.save(courseEntity);
                return course.isPresent();
            }
        }
        return false;
    }


    protected String getAuthorSet(PropertyLoader prop) {
        List<String> result = new ArrayList<>();
        for (Entry<Object, Object> entry : prop.getEntrySet()) {
            if (keyStartsWith(entry.getKey(), AUTHOR_KEY)) {
                result.add((String) entry.getValue());
            }
        }
        return result.stream().collect(Collectors.joining(AUTHOR_SEPARATOR));
    }


    protected boolean keyStartsWith(Object key, String start) {
        return ((String) key).startsWith(start);
    }


    protected int getNumber(String string) {
        try {
            return Integer.parseUnsignedInt(string);
        } catch (NumberFormatException e) {
            return 0;
        }
    }


    protected int getChapterNumber(int nest, String key) {
        int start = 0;
        int pos = key.indexOf(DOT);
        for (int i = 1; i < nest; i++) {
            start = pos + 1;
            pos = key.indexOf(DOT, start);
        }
        if (pos < 0) {
            pos = key.length();
        }
        return getNumber(key.substring(start, pos));
    }


    protected int countDots(String key) {
        int result = 0;
        for (int i = 0; i < key.length(); i++) {
            if (key.charAt(i) == DOT) {
                result++;
            }
        }
        return result;
    }


    protected String createKeyStarts(int number, String part) {
        return String.valueOf(number) + '.' + part;
    }


    Set<ChapterEntity> getChapters(CourseEntity course, PropertyLoader prop) {
        Set<ChapterEntity> result = new HashSet<>();
        int n;
        for (Entry<Object, Object> entry : prop.getEntrySet()) {
            String key = (String) entry.getKey();
            if (countDots(key) == 1 && key.endsWith(".") && (n = getChapterNumber(1, key)) != 0) {
                String[] names = ((String) entry.getValue()).split(NAME_SEPARATOR);
                ChapterEntity chapter = chapterService.create(course, n, names[0], names[1]);
                if (chapter != null) {
                    getChapterPartSet(chapter, prop);
                    result.add(chapter);
                }
            }
        }
        return result;
    }


    boolean partNumberExists(int number, Set<ChapterPartEntity> parts) {
        for (ChapterPartEntity part : parts) {
            if (part.getNumber() == number) {
                return true;
            }
        }
        return false;
    }


    Set<ChapterPartEntity> getChapterPartSet(ChapterEntity chapter, PropertyLoader prop) {
        Set<ChapterPartEntity> result = new HashSet<>();
        String keyStarts = createKeyStarts(chapter.getNumber(), "");
        for (Entry<Object, Object> entry : prop.getEntrySet()) {
            String key = (String) entry.getKey();
            if (key.startsWith(keyStarts) && countDots(key) == 2 && key.endsWith(".")
                    && getChapterNumber(2, key) != 0) {
                result.add(getChapterPart(chapter, 1, prop, keyStarts));
                break;
            }
        }
        int n;
        keyStarts = String.valueOf(chapter.getNumber()) + PART_SEPARATOR;
        for (Entry<Object, Object> entry : prop.getEntrySet()) {
            String key = (String) entry.getKey();
            if (key.startsWith(keyStarts) && countDots(key) == 2 && key.endsWith(".")
                    && getChapterNumber(2, key) != 0
                    && !partNumberExists(n = getChapterPartNumber(key), result)) {
                result.add(
                        getChapterPart(chapter, n, prop, key.substring(0, key.indexOf(DOT) + 1)));
                break;
            }
        }
        return result;
    }


    int getChapterPartNumber(String key) {
        return getNumber(key.substring(key.indexOf(PART_SEPARATOR) + 1, key.indexOf(DOT)));
    }


    ChapterPartEntity getChapterPart(ChapterEntity chapter, int number, PropertyLoader prop,
            String keyStarts) {
        String praxisKey = keyStarts + PRAXIS_PART;
        ChapterPartEntity chapterPart = chapterPartService
                .create(new ChapterPartEntity(chapter, number, prop.getProperty(praxisKey, "-")));
        if (chapterPart != null) {
            getSubChapterSet(chapterPart, prop, keyStarts);
            getPraxisSet(chapterPart, prop, praxisKey);
            getAdditionalSet(chapterPart, prop, keyStarts + ADDITIONAL_PART);
        }
        return chapterPart;
    }


    Set<SubChapterEntity> getSubChapterSet(ChapterPartEntity chapterPart, PropertyLoader prop,
            String keyStarts) {
        Set<SubChapterEntity> result = new HashSet<>();
        int n;
        for (Entry<Object, Object> entry : prop.getEntrySet()) {
            String key = (String) entry.getKey();
            if (key.startsWith(keyStarts) && countDots(key) == 2 && key.endsWith(".")
                    && (n = getChapterNumber(2, key)) != 0) {
                String[] nameRefs = ((String) entry.getValue()).split(NAME_SEPARATOR);
                SubChapterEntity subChapter = chapterPartService.createSub(new SubChapterEntity(0,
                        chapterPart, n, nameRefs[0], getReferenceTitleEntitySet(nameRefs)));
                if (subChapter != null) {
                    getSubSubChapterSet(subChapter, prop, key);
                    result.add(subChapter);
                }
            }
        }
        return result;
    }


    Set<SubSubChapterEntity> getSubSubChapterSet(SubChapterEntity subChapter, PropertyLoader prop,
            String keyStarts) {
        Set<SubSubChapterEntity> result = new HashSet<>();
        int n;
        for (Entry<Object, Object> entry : prop.getEntrySet()) {
            String key = (String) entry.getKey();
            if (key.startsWith(keyStarts) && key.endsWith(".")
                    && (n = getChapterNumber(3, key)) != 0) {
                String[] nameRefs = ((String) entry.getValue()).split(NAME_SEPARATOR);
                SubSubChapterEntity subSubChapter = chapterPartService
                        .createSubSub(new SubSubChapterEntity(0, subChapter, n, nameRefs[0],
                                getReferenceTitleEntitySet(nameRefs)));
                if (subSubChapter != null) {
                    result.add(subSubChapter);
                }
            }
        }
        return result;
    }


    Set<PraxisEntity> getPraxisSet(ChapterPartEntity chapterPart, PropertyLoader prop,
            String keyStarts) {
        Set<PraxisEntity> result = new HashSet<>();
        int n;
        for (Entry<Object, Object> entry : prop.getEntrySet()) {
            String key = (String) entry.getKey();
            if (key.startsWith(keyStarts) && (n = getChapterNumber(3, key)) != 0) {
                String[] nameRefs = ((String) entry.getValue()).split(NAME_SEPARATOR);
                PraxisEntity praxis = chapterPartService.createPraxis(new PraxisEntity(0,
                        chapterPart, n, nameRefs[0], getReferenceTitleEntitySet(nameRefs)));
                if (praxis != null) {
                    result.add(praxis);
                }
            }
        }
        return result;
    }


    Set<AdditionalEntity> getAdditionalSet(ChapterPartEntity chapterPart, PropertyLoader prop,
            String keyStarts) {
        Set<AdditionalEntity> result = new HashSet<>();
        int n;
        for (Entry<Object, Object> entry : prop.getEntrySet()) {
            String key = (String) entry.getKey();
            if (key.startsWith(keyStarts) && (n = getChapterNumber(3, key)) != 0) {
                String[] nameRefs = ((String) entry.getValue()).split(NAME_SEPARATOR);
                AdditionalEntity additional = chapterPartService
                        .createAdditional(new AdditionalEntity(0, chapterPart, n, nameRefs[0],
                                getReferenceTitleEntitySet(nameRefs)));
                if (additional != null) {
                    result.add(additional);
                }
            }
        }
        return result;
    }


    Set<LevelEntity> getLevelsSet(CourseEntity course, PropertyLoader prop) {
        Set<LevelEntity> result = new HashSet<>();
        Map<Integer, List<Integer>> levelMap = getLevelMap(prop);
        for (Entry<Integer, List<Integer>> entry : levelMap.entrySet()) {
            LevelEntity level = new LevelEntity(0, course, entry.getKey(), entry.getValue());
            result.add(levelService.create(level));
        }
        return result;
    }


    Map<Integer, List<Integer>> getLevelMap(PropertyLoader prop) {
        Map<Integer, List<Integer>> result = new HashMap<>();
        int n;
        for (Entry<Object, Object> entry : prop.getEntrySet()) {
            String key = (String) entry.getKey();
            if (key.startsWith(LEVEL_KEY)
                    && (n = getNumber(key.substring(LEVEL_KEY_LENGTH))) != 0) {
                List<Integer> chapterList = new ArrayList<>();
                result.put(n, chapterList);
                for (String chapterN : ((String) entry.getValue()).split(",")) {
                    if ((n = getNumber(chapterN)) > 0) {
                        chapterList.add(n);
                    } else {
                        return Collections.emptyMap();
                    }
                }
            }
        }
        return result;
    }


    Set<AdditionalMaterialsEntity> getAdditionalMaterials(PropertyLoader prop) {
        int n = 0;
        int max = 0;
        int current;
        for (Entry<Object, Object> entry : prop.getEntrySet()) {
            String key = (String) entry.getKey();
            if (keyStartsWith(key, ADDITIONAL_PART)) {
                n++;
                if (max < (current = getChapterNumber(2, key))) {
                    max = current;
                }
            }
        }
        if (n != max) {
            return Set.of();
        }
        Set<AdditionalMaterialsEntity> result = new HashSet<>(max);
        for (int i = 1; i <= max; i++) {
            String item = prop.getProperty(ADDITIONAL_PART + i);
            if (item == null) {
                break;
            }
            String[] part = item.split(NAME_SEPARATOR);
            Optional<AdditionalMaterialsEntity> additionalMaterial = additionalMaterialsService
                    .update(new AdditionalMaterialsEntity(0, i, part[0],
                            getReferenceTitleEntitySet(part)));
            if (additionalMaterial.isPresent()) {
                result.add(additionalMaterial.get());
            }
        }
        return result;
    }


    Set<ReferenceTitleEntity> getReferenceTitleEntitySet(String[] part) {
        return part.length == 1 ? Set.of()
                : referenceTitleService.create(Set.of(part[1].split(REFERENCE_SEPARATOR)));
    }

}
