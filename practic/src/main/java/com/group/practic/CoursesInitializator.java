package com.group.practic;

import com.group.practic.entity.CourseEntity;
import com.group.practic.service.ChapterService;
import com.group.practic.service.CourseService;
import com.group.practic.service.LevelService;
import com.group.practic.structure.SimpleChapterStructure;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
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

    public static final String REFERENCE_SEPARATOR = "<>";

    public static final char DOT = '.';

    public static final int CHAPTER_NESTING_LEVEL_MAX = 3;

    @Autowired
    CourseService courseService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private LevelService levelService;

    @Value("${refreshDB_on_start}")
    boolean refresh;


    @Autowired
    CoursesInitializator(CourseService courseService) {
        this.courseService = courseService;
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
                CourseEntity courseEntity = new CourseEntity();
                courseEntity.setAuthors(getAuthorSet(prop));
                courseEntity.setCourseType(prop.getProperty(TYPE_KEY, ""));
                courseEntity.setShortName(prop.getProperty(SHORT_NAME_KEY, ""));
                courseEntity.setName(prop.getProperty(NAME_KEY, ""));
                courseEntity.setPurpose(prop.getProperty(PURPOSE_KEY, ""));
                courseEntity.setDescription(prop.getProperty(DESCRIPTION_KEY, ""));
                courseEntity.setSlug(prop.getProperty(SLUG_KEY, ""));
                Optional<CourseEntity> course = courseService.create(courseEntity);
                if (course.isPresent()) {
                    chapterService.createMany(course.get(), getChapterList(prop));
                    levelService.createMany(course.get(), getLevelMap(prop));
                }
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
        return result.stream().collect(Collectors.joining("\n"));
    }


    protected Map<Integer, List<Integer>> getLevelMap(PropertyLoader prop) {
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
            } else {
                return Collections.emptyMap();
            }
        }
        return result;
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


    protected int getChapterNumber(int part, String key) {
        int start = 0;
        int pos = key.indexOf(DOT);
        for (int i = 1; i < part; i++) {
            start = pos + 1;
            pos = key.indexOf(DOT, start);
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


    protected List<SimpleChapterStructure> getChapterList(PropertyLoader prop) {
        SimpleChapterStructure root = new SimpleChapterStructure();
        addOffsprings(root, 1, "", prop);
        return root.getOffsprings();
    }


    protected void addOffsprings(SimpleChapterStructure parent, int nestingLevel, String chapterN,
            PropertyLoader prop) {
        int n;
        for (Entry<Object, Object> entry : prop.getEntrySet()) {
            String entryChapter = (String) entry.getKey();
            if (entryChapter.startsWith(chapterN) && countDots(entryChapter) == nestingLevel
                    && (n = getChapterNumber(nestingLevel, entryChapter)) != 0) {
                SimpleChapterStructure newChapter = new SimpleChapterStructure(n,
                        (String) entry.getValue());
                parent.newOffspring(newChapter);
                if (CHAPTER_NESTING_LEVEL_MAX > nestingLevel) {
                    addOffsprings(newChapter, nestingLevel + 1, entryChapter, prop);
                }
            }
        }
        parent.sortOffsprings();
    }

}
