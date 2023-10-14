package com.group.practic.service;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.repository.ChapterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ChapterServiceTest {

    @InjectMocks
    private ChapterService chapterService;

    @Mock
    private ChapterRepository chapterRepository;

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
        when(chapterRepository.findByCourseAndShortName(course, shortname)).thenReturn(existingChapter);

        ChapterEntity newChapter = new ChapterEntity(0, course, number, shortname, name);
        when(chapterRepository.save(newChapter)).thenReturn(newChapter);

        ChapterEntity result = chapterService.create(course, number, shortname, name);

        assertSame(newChapter, result);
    }

    @Test
    void testSaveChapter() {
        ChapterEntity chapter = new ChapterEntity(1, new CourseEntity(), 1, "chapter1", "Chapter 1");

        when(chapterRepository.save(chapter)).thenReturn(chapter);

        Optional<ChapterEntity> result = chapterService.save(chapter);

        assertTrue(result.isPresent());
        assertSame(chapter, result.get());
    }

    @Test
    void testGetChapterSucceedNumber() {
        List<ChapterEntity> chapterList = new ArrayList<>();
        chapterList.add(new ChapterEntity(1, new CourseEntity(), 1, "chapter1", "Chapter 1"));
        chapterList.add(new ChapterEntity(2, new CourseEntity(), 2, "chapter2", "Chapter 2"));
        chapterList.add(new ChapterEntity(3, new CourseEntity(), 3, "chapter3", "Chapter 3"));

        int result = ChapterService.getChapterSucceedNumber(chapterList);

        assertEquals(4, result);
    }

    @Test
    void testGetChapterByNumber() {
        CourseEntity course = new CourseEntity();
        int number = 1;
        ChapterEntity chapter = new ChapterEntity(1, course, number, "chapter1", "Chapter 1");
        when(chapterRepository.findByCourseAndNumber(course, number)).thenReturn(Optional.of(chapter));

        Optional<ChapterEntity> result = chapterService.getChapterByNumber(course, number);

        assertTrue(result.isPresent());
        assertSame(chapter, result.get());
    }

    @Test
    void testGetChapterByShortName() {
        String shortName = "chapter1";
        ChapterEntity chapter = new ChapterEntity(1, new CourseEntity(), 1, shortName, "Chapter 1");
        when(chapterRepository.findByShortName(shortName)).thenReturn(Optional.of(chapter));

        Optional<ChapterEntity> result = chapterService.getByShortName(shortName);

        assertTrue(result.isPresent());
        assertSame(chapter, result.get());
    }
}