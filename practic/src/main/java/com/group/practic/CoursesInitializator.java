package com.group.practic;

import com.group.practic.service.CourseService;
import com.group.practic.structure.SimpleChapterStructure;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CoursesInitializator {

  public static final String COURSE_MASK = ".course";

  public static final String NAME_KEY = "name";
  public static final String AUTHOR_KEY = "author";
  public static final String TYPE_KEY = "type";
  public static final String DESCRIPTION_KEY = "description";
  public static final String PURPOSE_KEY = "purpose";
  public static final String LEVEL_KEY = "level";
  private static final int LEVEL_KEY_LENGTH = LEVEL_KEY.length();

  public static final String SEPARATOR = File.separator;
  public static final String REFERENCE_SEPARATOR = "<>";

  public static final char DOT = '.';
  public static final int CHAPTER_NESTING_LEVEL_MAX = 3;

  CourseService courseService;


  @Autowired
  CoursesInitializator(CourseService courseService) {
    this.courseService = courseService;
  }


  @PostConstruct
  void initialize() {
    File[] files = new File(".").listFiles(f -> f.isFile() && f.getName().endsWith(COURSE_MASK));
    for (File file : files) {
      initCourse(file.getName());
    }
  }


  public boolean initCourse(String filename) {
    if (PropertyLoader.isComprehendedString(filename)) {
      PropertyLoader prop = new PropertyLoader(filename);
      if (prop.initialized) {
        Long courseId = courseService.create(getAuthorSet(prop), prop.getProperty(TYPE_KEY, ""),
            prop.getProperty(NAME_KEY, ""), prop.getProperty(PURPOSE_KEY, ""),
            prop.getProperty(DESCRIPTION_KEY, ""), getLevelMap(prop), getChapterList(prop));
        return courseId > 0;
      }
    }
    return false;
  }


  protected Set<String> getAuthorSet(PropertyLoader prop) {
    Set<String> result = new HashSet<>();
    for (Entry<Object, Object> entry : prop.getEntrySet()) {
      if (keyStartsWith(entry.getKey(), AUTHOR_KEY)) {
        result.add((String) entry.getValue());
      }
    }
    return result;
  }


  protected Map<Integer, List<Integer>> getLevelMap(PropertyLoader prop) {
    Map<Integer, List<Integer>> result = new HashMap<>();
    int n;
    for (Entry<Object, Object> entry : prop.getEntrySet()) {
      String key = (String) entry.getKey();
      if (key.startsWith(LEVEL_KEY) && (n = getNumber(key.substring(LEVEL_KEY_LENGTH))) != 0) {
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
        SimpleChapterStructure newChapter =
            new SimpleChapterStructure(n, (String) entry.getValue());
        parent.newOffspring(newChapter);
        if (CHAPTER_NESTING_LEVEL_MAX > nestingLevel)
          addOffsprings(newChapter, nestingLevel + 1, entryChapter, prop);
      }
    }
    parent.sortOffsprings();
  }

}
