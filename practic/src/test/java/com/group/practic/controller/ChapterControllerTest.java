package com.group.practic.controller;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.service.ChapterService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j

 class ChapterControllerTest {

    @Mock
    private ChapterService chapterService;

    @InjectMocks
    private ChapterController chapterController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testGetChapterById() {
        long chapterId = 1L;
        ChapterEntity chapter = new ChapterEntity();
        chapter.setId(chapterId);
        when(chapterService.get(chapterId)).thenReturn(java.util.Optional.of(chapter));
        ResponseEntity<ChapterEntity> response = chapterController.get(chapterId);

        assertEquals(response.getBody().getId(), chapter.getId());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(chapter, response.getBody());
    }

    @Test
     void testGetChapterByIdNotFound() {
        long chapterId = 1L;
        ChapterEntity chapter = new ChapterEntity();
        chapter.setId(chapterId);
        long chapterNotId = 2L;
        ChapterEntity chapterNew = new ChapterEntity();
        chapterNew.setId(chapterNotId);
        when(chapterService.get(chapterId)).thenReturn(java.util.Optional.empty());
        ResponseEntity<ChapterEntity> response = chapterController.get(chapterId);

        assertNotEquals(chapter.getId(), chapterNotId);
        assertNotEquals(chapter.getId(), chapterNew.getId());

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
     void testGetChapterByShortName() {
        String shortName = "chapter-1";
        ChapterEntity chapter = new ChapterEntity();
        chapter.setShortName(shortName);
        when(chapterService.getByShortName(shortName)).thenReturn(java.util.Optional.of(chapter));
        ResponseEntity<ChapterEntity> response = chapterController.getByShortName(shortName);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(chapter, response.getBody());
    }

    @Test
     void testGetChapterByShortNameNotFound() {
        String shortName = "chapter-1";
        when(chapterService.getByShortName(shortName)).thenReturn(java.util.Optional.empty());

        ResponseEntity<ChapterEntity> response = chapterController.getByShortName(shortName);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}