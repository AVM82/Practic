package com.group.practic.service;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.repository.ChapterRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ChapterService {

    ChapterRepository chapterRepository;


    @Autowired
    public ChapterService(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
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

}
