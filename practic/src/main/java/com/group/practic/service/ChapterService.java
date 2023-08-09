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


  public SubChapterEntity createSub(ChapterEntity chapter, int number, List<String> p) {
    SubChapterEntity subChapter = new SubChapterEntity(0, chapter, number, p.get(0), p.get(1));
    return subChapterRepository.save(subChapter);
  }


  public SubSubChapterEntity createSubSub(SubChapterEntity subChapter, int number, List<String> p) {
    SubSubChapterEntity subSubChapter =
        new SubSubChapterEntity(0, subChapter, number, p.get(0), p.get(1));
    return subSubChapterRepository.save(subSubChapter);
  }


  public ChapterEntity create(CourseEntity course, int number, String name) {
    ChapterEntity chapter = new ChapterEntity();
    chapter.setCourse(course);
    chapter.setName(name);
    chapter.setNumber(number);
    return chapterRepository.saveAndFlush(chapter);
  }


  public List<ChapterEntity> createMany(CourseEntity course,
      List<SimpleChapterStructure> chapters) {
    List<ChapterEntity> result = new ArrayList<>();
    for (SimpleChapterStructure chapterSource : chapters) {
      ChapterEntity chapter = create(course, chapterSource.getNumber(), chapterSource.getHeader());
      addSubChapters(chapter, chapterSource.getOffsprings());
      result.add(chapterRepository.save(chapter));
    }
    return result;
  }


  protected void addSubChapters(ChapterEntity chapter,
      List<SimpleChapterStructure> subChapterList) {
    for (SimpleChapterStructure subChapterSource : subChapterList) {
      SubChapterEntity subChapter = createSub(chapter, subChapterSource.getNumber(),
          extractNameAndRefs(subChapterSource.getHeader()));
      addSubSubChapters(subChapter, subChapterSource.getOffsprings());
      chapter.addSubChapter(subChapter);
    }
  }


  protected void addSubSubChapters(SubChapterEntity subChapter,
      List<SimpleChapterStructure> subSubChapterList) {
    for (SimpleChapterStructure subSubChapterSource : subSubChapterList) {
      SubSubChapterEntity subSubChapter = createSubSub(subChapter, subSubChapterSource.getNumber(),
          extractNameAndRefs(subSubChapterSource.getHeader()));
      subChapter.addSubSubChapter(subSubChapter);
    }
  }


  protected List<String> extractNameAndRefs(String source) {
    List<String> result = new ArrayList<>();
    String[] part = source.split(CoursesInitializator.REFERENCE_SEPARATOR);
    result.add(part[0]);
    result.add(part.length > 1 ? part[1] : null);
    return result;
  }


  public Optional<ChapterEntity> get(long id) {
    return chapterRepository.findById(id);
  }

}
