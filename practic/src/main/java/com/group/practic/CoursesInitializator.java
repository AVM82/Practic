package com.group.practic;

import com.group.practic.service.CourseService;
import com.group.practic.structure.SimpleChapterStructure;
import java.util.Set;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import jakarta.annotation.PostConstruct;
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

  CourseService courseService;


  @Autowired
  CoursesInitializator(CourseService courseService) {
    this.courseService = courseService;
  }

  @PostConstruct
  void initialize() {
    File[] files = new File(".").listFiles(f -> f.isFile() && f.getName().endsWith(COURSE_MASK));
    for (File file : files)
      if (initCourse(file.getName())) {
        // logger
      }
  }

  public boolean initCourse(String filename) {
    if (PropertyLoader.isComprehendedString(filename)) {
      PropertyLoader propCourse = new PropertyLoader(filename);
      if (propCourse.initialized) {
        Long courseId = courseService.create(getAuthorSet(propCourse),
            propCourse.getProperty(TYPE_KEY, ""), propCourse.getProperty(NAME_KEY, ""),
            propCourse.getProperty(PURPOSE_KEY, ""), propCourse.getProperty(DESCRIPTION_KEY, ""),
            getLevelMap(propCourse), getChapterList(propCourse));
        return courseId > 0;
      }
    }
    return false;
  }

  protected Set<String> getAuthorSet(PropertyLoader prop) {
    Set<String> result = new HashSet<>();
    for (Entry<Object, Object> entry : prop.getEntrySet())
      if (keyStartsWith(entry.getKey(), AUTHOR_KEY))
        result.add((String) entry.getValue());
    return result;
  }

  protected Map<Integer, List<Integer>> getLevelMap(PropertyLoader prop) {
    Map<Integer, List<Integer>> result = new HashMap<>();
    int n;
    for (Entry<Object, Object> entry : prop.getEntrySet()) {
      String key = (String) entry.getKey();
      if (key.startsWith(LEVEL_KEY)) {
        if ((n = getNumber(key.substring(LEVEL_KEY_LENGTH))) == 0)
          return null;
        List<Integer> chapterList = new ArrayList<>();
        result.put(n, chapterList);
        for (String chapterN : ((String) entry.getValue()).split(","))
          if ((n = getNumber(chapterN)) > 0)
            chapterList.add(n);
          else
            return null;
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
    int pos = key.indexOf('.');
    for (int i = 1; i < part; i++) {
      start = pos + 1;
      pos = key.indexOf('.', start);
    }
    return getNumber(key.substring(start, pos));
  }

  protected int countDots(String key) {
    int result = 0;
    for (int i = 0; i < key.length(); i++)
      if (key.charAt(i) == DOT)
        result++;
    return result;
  }

  protected List<SimpleChapterStructure> getChapterList(PropertyLoader prop) {
    SimpleChapterStructure root = new SimpleChapterStructure();
    int n;
    for (Entry<Object, Object> entry1 : prop.getEntrySet()) {
      String chapter1 = (String) entry1.getKey();
      if (countDots(chapter1) == 1) {
        if ((n = getChapterNumber(1, chapter1)) == 0)
          return null;
        SimpleChapterStructure chapter1level =
            new SimpleChapterStructure(n, (String) entry1.getValue());
        root.newOffspring(chapter1level);
        for (Entry<Object, Object> entry2 : prop.getEntrySet()) {
          String chapter2 = (String) entry2.getKey();
          if (countDots(chapter2) == 2 && chapter2.startsWith(chapter1)) {
            if ((n = getChapterNumber(2, chapter2)) == 0)
              return null;
            SimpleChapterStructure chapter2level =
                new SimpleChapterStructure(n, (String) entry2.getValue());
            chapter1level.newOffspring(chapter2level);
            for (Entry<Object, Object> entry3 : prop.getEntrySet()) {
              String chapter3 = (String) entry3.getKey();
              if (countDots(chapter3) == 3 && chapter3.startsWith(chapter2)) {
                if ((n = getChapterNumber(3, chapter3)) == 0)
                  return null;
                SimpleChapterStructure chapter3level =
                    new SimpleChapterStructure(n, (String) entry3.getValue());
                chapter2level.newOffspring(chapter3level);
              }
            }
            chapter2level.sortOffsprings();
          }
        }
        chapter1level.sortOffsprings();
      }
    }
    root.sortOffsprings();
    return root.getOffsprings();
  }

}
