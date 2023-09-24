package com.group.practic.service;

import com.group.practic.PropertyLoader;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.repository.ChapterRepository;
import com.group.practic.util.PropertyUtil;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ChapterService {

    ChapterRepository chapterRepository;

    ChapterPartService chapterPartService;


    @Autowired
    public ChapterService(ChapterRepository chapterRepository,
            ChapterPartService chapterPartService) {
        this.chapterRepository = chapterRepository;
        this.chapterPartService = chapterPartService;
    }


    public Optional<ChapterEntity> get(long id) {
        return chapterRepository.findById(id);
    }


    public List<ChapterEntity> getAll(CourseEntity course) {
        return chapterRepository.findAllByCourseOrderByNumberAsc(course);
    }


    public ChapterEntity create(CourseEntity course, int number, String shortname, String name) {
        ChapterEntity chapter = chapterRepository.findByCourseAndShortName(course, shortname);
        return chapter != null ? chapter
                : chapterRepository.save(new ChapterEntity(0, course, number, shortname, name));
    }


    public Optional<ChapterEntity> save(ChapterEntity chapter) {
        return Optional.ofNullable(chapterRepository.save(chapter));
    }


    protected int getChapterListSuitNumber(List<ChapterEntity> chapterList, int number) {
        if (number == 0) {
            return getChapterSucceedNumber(chapterList);
        }
        boolean exists = false;
        for (ChapterEntity chapter : chapterList) {
            if (number == chapter.getNumber()) {
                exists = true;
                break;
            }
        }
        int n = number;
        while (exists) {
            exists = false;
            for (ChapterEntity chapter : chapterList) {
                if (chapter.getNumber() == n) {
                    chapter.setNumber(++n);
                    chapterRepository.save(chapter);
                    exists = true;
                }
            }
        }
        return number;
    }


    static int getChapterSucceedNumber(List<ChapterEntity> chapterList) {
        int number = 0;
        for (ChapterEntity chapter : chapterList) {
            if (number < chapter.getNumber()) {
                number = chapter.getNumber();
            }
        }
        return number + 1;
    }


    public Optional<ChapterEntity> getChapterByNumber(CourseEntity course, int number) {
        return chapterRepository.findByCourseAndNumber(course, number);
    }


    public Optional<ChapterEntity> getByShortName(String shortName) {
        return chapterRepository.findByShortName(shortName);
    }


    public Set<ChapterEntity> getChapters(CourseEntity course, PropertyLoader prop) {
        Set<ChapterEntity> result = new HashSet<>();
        int n;
        for (Entry<Object, Object> entry : prop.getEntrySet()) {
            String key = (String) entry.getKey();
            if (PropertyUtil.countDots(key) == 1 && key.endsWith(".")
                    && (n = PropertyUtil.getChapterNumber(1, key)) != 0) {
                String[] names = ((String) entry.getValue()).split(PropertyUtil.NAME_SEPARATOR);
                ChapterEntity chapter = create(course, n, names[0], names[1]);
                if (chapter != null) {
                    chapterPartService.getChapterPartSet(chapter, prop);
                    result.add(chapter);
                }
            }
        }
        return result;
    }

}
