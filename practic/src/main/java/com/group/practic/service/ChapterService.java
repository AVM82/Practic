package com.group.practic.service;

import com.group.practic.PropertyLoader;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.repository.ChapterRepository;
import com.group.practic.util.PropertyUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ChapterService {

    ChapterRepository chapterRepository;

    ChapterPartService chapterPartService;

    TopicReportService topicReportService;

    @Autowired
    public ChapterService(ChapterRepository chapterRepository,
            ChapterPartService chapterPartService, TopicReportService topicReportService) {
        this.chapterRepository = chapterRepository;
        this.chapterPartService = chapterPartService;
        this.topicReportService = topicReportService;
    }


    public Optional<ChapterEntity> get(long id) {
        return chapterRepository.findById(id);
    }


    public Optional<ChapterEntity> get(CourseEntity course, int number) {
        return chapterRepository.findByCourseAndNumber(course, number);
    }


    public List<ChapterEntity> getAll(CourseEntity course) {
        return chapterRepository.findAllByCourseOrderByNumberAsc(course);
    }


    public ChapterEntity createOrUpdate(ChapterEntity newChapter) {
        Optional<ChapterEntity> chapter = get(newChapter.getCourse(), newChapter.getNumber());
        if (chapter.isPresent()) {
            return chapter.get().equals(newChapter) ? chapter.get()
                : chapterRepository.save(chapter.get().update(newChapter));
        }
        return chapterRepository.save(newChapter); 
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


    public Optional<ChapterEntity> getByShortName(String shortName) {
        return chapterRepository.findByShortName(shortName);
    }


    public List<ChapterEntity> getChapters(CourseEntity course, PropertyLoader prop) {
        List<ChapterEntity> result = new ArrayList<>();
        int n;
        for (Entry<Object, Object> entry : prop.getEntrySet()) {
            String key = (String) entry.getKey();
            if (PropertyUtil.countDots(key) == 1 && key.endsWith(".")
                    && (n = PropertyUtil.getChapterNumber(1, key)) != 0) {
                String[] names = ((String) entry.getValue()).split(PropertyUtil.NAME_SEPARATOR);
                String fullName = names.length > 1 ? names[1] : names[0];
                ChapterEntity chapter = createOrUpdate(
                        new ChapterEntity(course, n, names[0], fullName, 
                                getSkills(key, prop)));
                if (chapter != null) {
                    chapterPartService.getChapterPartSet(chapter, prop);
                    topicReportService.getChapterTopics(chapter, prop);
                    result.add(chapter);
                }
            }
        }
        return result;
    }


    protected List<String> getSkills(String keyStarts, PropertyLoader prop) {
        String key = keyStarts + PropertyUtil.SKILL_PART;
        for (Entry<Object, Object> entry : prop.getEntrySet()) {
            if (((String) entry.getKey()).equals(key)) {
                return List.of(((String) entry.getValue()).split(PropertyUtil.SKILL_SEPARATOR));
            }
        }
        return List.of();
    }

}
