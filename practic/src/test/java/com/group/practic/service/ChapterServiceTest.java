package com.group.practic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.group.practic.PropertyLoader;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.ChapterPartEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.QuizEntity;
import com.group.practic.entity.TopicReportEntity;
import com.group.practic.repository.ChapterRepository;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ChapterServiceTest {

    @InjectMocks
    private ChapterService chapterService;

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private PropertyLoader propertyLoader;

    @Mock
    private ChapterPartService chapterPartService;

    @Mock
    private TopicReportService topicReportService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetChapterById() {
        long chapterId = 1;
        ChapterEntity chapter = new ChapterEntity();
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));

        Optional<ChapterEntity> result = chapterService.get(chapterId);

        assertTrue(result.isPresent());
        assertSame(chapter, result.get());
    }

    @Test
    void testGetAllChapters() {
        CourseEntity course = new CourseEntity();
        List<ChapterEntity> chapterList = new ArrayList<>();
        when(chapterRepository.findAllByCourseOrderByNumberAsc(course)).thenReturn(chapterList);

        List<ChapterEntity> result = chapterService.getAll(course);

        assertSame(chapterList, result);
    }

    @Test
    void testCreateChapter() {
        CourseEntity course = new CourseEntity();
        int number = 1;
        String shortname = "chapter1";
        String name = "Chapter 1";
        ChapterEntity existingChapter = null;
        when(chapterRepository.findByCourseAndNumber(course, number))
                .thenReturn(Optional.ofNullable(existingChapter));

        ChapterEntity newChapter =
                new ChapterEntity(0, course, number, shortname, new QuizEntity());
        when(chapterRepository.save(newChapter)).thenReturn(newChapter);

        ChapterEntity result = chapterService.createOrUpdate(newChapter);

        assertSame(newChapter, result);
    }

    @Test
    void testSaveChapter() {
        ChapterEntity chapter = new ChapterEntity(
                1, new CourseEntity(), 1, "chapt1", new QuizEntity());

        when(chapterRepository.save(chapter)).thenReturn(chapter);

        Optional<ChapterEntity> result = chapterService.save(chapter);

        assertTrue(result.isPresent());
        assertSame(chapter, result.get());
    }

    @Test
    void testGetChapterSucceedNumber() {
        List<ChapterEntity> chapterList = new ArrayList<>();
        chapterList.add(
                new ChapterEntity(1, new CourseEntity(), 1, "chapter1", new QuizEntity()));
        chapterList.add(
                new ChapterEntity(2, new CourseEntity(), 2, "chapter2", new QuizEntity()));
        chapterList.add(
                new ChapterEntity(3, new CourseEntity(), 3, "chapter3", new QuizEntity()));

        int result = ChapterService.getChapterSucceedNumber(chapterList);

        assertEquals(4, result);
    }

    @Test
    void testGetChapterByNumber() {
        CourseEntity course = new CourseEntity();
        int number = 1;
        ChapterEntity chapter = new ChapterEntity(1, course, number, "chapter1", new QuizEntity());
        when(chapterRepository.findByCourseAndNumber(course, number))
                .thenReturn(Optional.of(chapter));

        Set<Map.Entry<Object, Object>> entry = new HashSet<>();
        entry.add(new AbstractMap.SimpleEntry<>("3.", "new<>chapter"));

        when(propertyLoader.getEntrySet()).thenReturn(entry);
        when(chapterRepository.save(any(ChapterEntity.class))).thenReturn(chapter);
        when(chapterPartService.getChapterPartSet(
                any(ChapterEntity.class), any(PropertyLoader.class)))
                .thenReturn(List.of(new ChapterPartEntity()));
        when(topicReportService.getChapterTopics(
                any(ChapterEntity.class), any(PropertyLoader.class)))
                .thenReturn(List.of(new TopicReportEntity()));

        List<ChapterEntity> result = chapterService.getChapters(course, propertyLoader);

        assertFalse(result.isEmpty());
        assertSame(chapter, result.get(0));
    }

    @Test
    void testGetChapterByShortName() {
        String shortName = "chapter1";
        ChapterEntity chapter = new ChapterEntity(
                1, new CourseEntity(), 1, shortName, new QuizEntity());
        when(chapterRepository.findByShortName(shortName)).thenReturn(Optional.of(chapter));

        Optional<ChapterEntity> result = chapterService.getByShortName(shortName);

        assertTrue(result.isPresent());
        assertSame(chapter, result.get());
    }
}
