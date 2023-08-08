package com.group.practic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.group.practic.CoursesInitializator;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.SubChapterEntity;
import com.group.practic.entity.SubSubChapterEntity;
import com.group.practic.repository.ChapterRepository;
import com.group.practic.repository.SubChapterRepository;
import com.group.practic.repository.SubSubChapterRepository;
import com.group.practic.structure.SimpleChapterStructure;

@Service
public class ChapterService {

	@Autowired
	ChapterRepository chapterRepository;

	@Autowired
	SubChapterRepository subChapterRepository;

	@Autowired
	SubSubChapterRepository subSubChapterRepository;

	public SubChapterEntity createSub(CourseEntity course, ChapterEntity chapter, int number, String name,
			String refs) {
		SubChapterEntity subChapter = new SubChapterEntity(0, course, chapter, number, name, refs);
		return subChapterRepository.save(subChapter);
	}

	public SubSubChapterEntity createSubSub(CourseEntity course, SubChapterEntity subChapter, int number, String name,
			String refs) {
		SubSubChapterEntity subSubChapter = new SubSubChapterEntity(0, course, subChapter, number, name, refs);
		return subSubChapterRepository.save(subSubChapter);
	}

	public ChapterEntity create(CourseEntity course, int number, String name) {
		ChapterEntity chapter = new ChapterEntity();
		chapter.setCourse(course);
		chapter.setName(name);
		chapter.setNumber(number);
		return chapterRepository.saveAndFlush(chapter);
	}

	public List<ChapterEntity> createMany(CourseEntity course, List<SimpleChapterStructure> chapters) {
		List<ChapterEntity> result = new ArrayList<>();
		for (SimpleChapterStructure chapterSource : chapters) {
			ChapterEntity chapter = create(course, chapterSource.getNumber(), chapterSource.getHeader());
			for (SimpleChapterStructure subChapterSource : chapterSource.getOffsprings()) {
				String[] partSub = subChapterSource.getHeader().split(CoursesInitializator.REFERENCE_SEPARATOR);
				SubChapterEntity subChapter = createSub(course, chapter, subChapterSource.getNumber(), partSub[0],
						partSub.length > 1 ? partSub[1] : "");
				chapter.addSubChapter(subChapter);
				for (SimpleChapterStructure subSubChapterSource : subChapterSource.getOffsprings()) {
					String[] partSubSub = subSubChapterSource.getHeader()
							.split(CoursesInitializator.REFERENCE_SEPARATOR);
					SubSubChapterEntity subSubChapter = createSubSub(course, subChapter,
							subSubChapterSource.getNumber(), partSubSub[0], partSubSub.length > 1 ? partSubSub[1] : "");
					subChapter.addSubSubChapter(subSubChapter);
				}
			}
			result.add(chapterRepository.save(chapter));
		}
		return result;
	}

	public Optional<ChapterEntity> get(int id) {
		return chapterRepository.findById(id);
	}
}
