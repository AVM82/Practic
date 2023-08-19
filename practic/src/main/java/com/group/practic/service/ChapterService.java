package com.group.practic.service;

import com.group.practic.CoursesInitializator;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.SubChapterEntity;
import com.group.practic.entity.SubSubChapterEntity;
import com.group.practic.repository.ChapterRepository;
import com.group.practic.repository.SubChapterRepository;
import com.group.practic.repository.SubSubChapterRepository;
import com.group.practic.structure.SimpleChapterStructure;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ChapterService {

    @Autowired
    ChapterRepository chapterRepository;

    @Autowired
    SubChapterRepository subChapterRepository;

    @Autowired
    SubSubChapterRepository subSubChapterRepository;

    @Autowired
    CourseService courseService;


    public Optional<ChapterEntity> get(long id) {
        return chapterRepository.findById(id);
    }


    public Optional<SubChapterEntity> getSub(long id) {
        return subChapterRepository.findById(id);
    }


    public Optional<SubSubChapterEntity> getSubSub(long id) {
        return subSubChapterRepository.findById(id);
    }


    public Optional<ChapterEntity> create(long courseId, int number, String shortname,
            String name) {
        Optional<CourseEntity> course = courseService.get(courseId);
        if (course.isEmpty()) {
            return Optional.empty();
        }
        return create(course.get(), number, shortname, name);
    }


    public Optional<ChapterEntity> create(CourseEntity course, int number, String shortname,
            String name) {
        List<ChapterEntity> chapterList = chapterRepository.findAllByCourseAndShortName(course,
                shortname);
        if (!chapterList.isEmpty()) {
            return Optional.empty();
        }
        chapterList = chapterRepository.findAllByCourse(course);
        if ((number = getChapterListSuitNumber(chapterList, number)) == 0) {
            return Optional.empty();
        }
        return Optional.ofNullable(
                chapterRepository.save(new ChapterEntity(course, number, shortname, name)));
    }


    public Optional<SubChapterEntity> createSub(long chapterId, int number, String name) {
        Optional<ChapterEntity> chapter = get(chapterId);
        if (chapter.isEmpty()) {
            return Optional.empty();
        }
        SubChapterEntity subChapter = new SubChapterEntity(chapter.get(), number, name);
        return Optional.ofNullable(subChapterRepository.save(subChapter));
    }


    public SubChapterEntity createSub(ChapterEntity chapter, int number, List<String> p) {
        SubChapterEntity subChapter = new SubChapterEntity(0, chapter, number, p.get(0), p.get(1));
        return subChapterRepository.save(subChapter);
    }


    public SubSubChapterEntity createSubSub(SubChapterEntity subChapter, int number,
            List<String> p) {
        SubSubChapterEntity subSubChapter = new SubSubChapterEntity(0, subChapter, number, p.get(0),
                p.get(1));
        return subSubChapterRepository.save(subSubChapter);
    }


    public List<ChapterEntity> createMany(CourseEntity course,
            List<SimpleChapterStructure> chapters) {
        List<ChapterEntity> result = new ArrayList<>();
        for (SimpleChapterStructure chapterSource : chapters) {
            List<String> names = extractParts(chapterSource.getHeader());
            Optional<ChapterEntity> chapter = create(course, chapterSource.getNumber(),
                    names.get(0), names.get(1));
            if (chapter.isPresent()) {
                addSubChapters(chapter.get(), chapterSource.getOffsprings());
                result.add(chapterRepository.save(chapter.get()));
            }
        }
        return result;
    }


    protected void addSubChapters(ChapterEntity chapter,
            List<SimpleChapterStructure> subChapterList) {
        for (SimpleChapterStructure subChapterSource : subChapterList) {
            SubChapterEntity subChapter = createSub(chapter, subChapterSource.getNumber(),
                    extractParts(subChapterSource.getHeader()));
            addSubSubChapters(subChapter, subChapterSource.getOffsprings());
            chapter.addSubChapter(subChapter);
        }
    }


    protected void addSubSubChapters(SubChapterEntity subChapter,
            List<SimpleChapterStructure> subSubChapterList) {
        for (SimpleChapterStructure subSubChapterSource : subSubChapterList) {
            SubSubChapterEntity subSubChapter = createSubSub(subChapter,
                    subSubChapterSource.getNumber(), extractParts(subSubChapterSource.getHeader()));
            subChapter.addSubSubChapter(subSubChapter);
        }
    }


    protected List<String> extractParts(String source) {
        List<String> result = new ArrayList<>();
        String[] part = source.split(CoursesInitializator.REFERENCE_SEPARATOR);
        result.add(part[0]);
        result.add(part.length > 1 ? part[1] : null);
        return result;
    }


    protected int getChapterListSuitNumber(List<ChapterEntity> chapterList, int number) {
        if (number == 0) {
            return getChapterSucceedingNumber(chapterList);
        }
        boolean exists = false;
        for (ChapterEntity chapter : chapterList) {
            if (number == chapter.getNumber()) {
                exists = true;
                break;
            }
        }
        if (exists) {
            int n = number;
            do {
                exists = false;
                for (ChapterEntity chapter : chapterList) {
                    if (chapter.getNumber() == n) {
                        chapter.setNumber(++n);
                        chapterRepository.save(chapter);
                        exists = true;
                        break;
                    }
                }
            } while (exists);
        }
        return number;
    }


    protected int getChapterSucceedingNumber(List<ChapterEntity> chapterList) {
        int number = 0;
        for (ChapterEntity chapter : chapterList) {
            if (number < chapter.getNumber()) {
                number = chapter.getNumber();
            }
        }
        return number + 1;
    }

}
