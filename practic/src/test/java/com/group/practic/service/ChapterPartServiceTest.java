package com.group.practic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.group.practic.entity.AdditionalEntity;
import com.group.practic.entity.ChapterPartEntity;
import com.group.practic.entity.PraxisEntity;
import com.group.practic.entity.SubChapterEntity;
import com.group.practic.entity.SubSubChapterEntity;
import com.group.practic.repository.AdditionalRepository;
import com.group.practic.repository.ChapterPartRepository;
import com.group.practic.repository.PraxisRepository;
import com.group.practic.repository.SubChapterRepository;
import com.group.practic.repository.SubSubChapterRepository;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


class ChapterPartServiceTest {

    @InjectMocks
    private ChapterPartService chapterPartService;

    @Mock
    private ChapterPartRepository chapterPartRepository;

    @Mock
    private SubChapterRepository subChapterRepository;

    @Mock
    private SubSubChapterRepository subSubChapterRepository;

    @Mock
    private PraxisRepository praxisRepository;

    @Mock
    private AdditionalRepository additionalRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateChapterPart() {
        ChapterPartEntity chapterPartEntity = new ChapterPartEntity();

        when(chapterPartRepository.findByChapterAndNumber(
                chapterPartEntity.getChapter(), chapterPartEntity.getNumber())).thenReturn(null);

        when(chapterPartRepository.save(chapterPartEntity)).thenReturn(chapterPartEntity);

        ChapterPartEntity result = chapterPartService.create(chapterPartEntity);

        assertNotNull(result);
        assertEquals(chapterPartEntity, result);
    }

    @Test
    void testCreateSubChapter() {
        SubChapterEntity subChapterEntity = new SubChapterEntity();

        when(subChapterRepository.findByChapterPartAndNumberAndName(
                subChapterEntity.getChapterPart(), subChapterEntity.getNumber(),
                subChapterEntity.getName())).thenReturn(null);

        when(subChapterRepository.save(subChapterEntity)).thenReturn(subChapterEntity);

        SubChapterEntity result = chapterPartService.createSub(subChapterEntity);

        assertNotNull(result);
        assertEquals(subChapterEntity, result);
    }

    @Test
    void testGetSub() {
        long subChapterId = 1;
        SubChapterEntity subChapterEntity = new SubChapterEntity();

        when(subChapterRepository.findById(subChapterId)).thenReturn(Optional.of(subChapterEntity));

        Optional<SubChapterEntity> result = chapterPartService.getSub(subChapterId);

        assertTrue(result.isPresent());
        assertEquals(subChapterEntity, result.get());
    }

    @Test
    void testGetSubSub() {
        long subSubChapterId = 1;
        SubSubChapterEntity subSubChapterEntity = new SubSubChapterEntity();

        when(subSubChapterRepository.findById(subSubChapterId))
                .thenReturn(Optional.of(subSubChapterEntity));

        Optional<SubSubChapterEntity> result = chapterPartService.getSubSub(subSubChapterId);

        assertTrue(result.isPresent());
        assertEquals(subSubChapterEntity, result.get());
    }

    @Test
    void testCreateSubSubChapter() {
        SubSubChapterEntity subSubChapterEntity = new SubSubChapterEntity();

        when(subSubChapterRepository.findBySubChapterAndNumberAndName(
                subSubChapterEntity.getSubChapter(), subSubChapterEntity.getNumber(),
                subSubChapterEntity.getName())).thenReturn(null);

        when(subSubChapterRepository.save(subSubChapterEntity)).thenReturn(subSubChapterEntity);

        SubSubChapterEntity result = chapterPartService.createSubSub(subSubChapterEntity);

        assertNotNull(result);
        assertEquals(subSubChapterEntity, result);
    }

    @Test
    void testCreatePraxis() {
        PraxisEntity praxisEntity = new PraxisEntity();

        when(praxisRepository.findByChapterPartAndNumberAndName(
                praxisEntity.getChapterPart(), praxisEntity.getNumber(),
                praxisEntity.getName())).thenReturn(null);

        when(praxisRepository.save(praxisEntity)).thenReturn(praxisEntity);

        PraxisEntity result = chapterPartService.createPraxis(praxisEntity);

        assertNotNull(result);
        assertEquals(praxisEntity, result);
    }

    @Test
    void testCreateAdditional() {
        AdditionalEntity additionalEntity = new AdditionalEntity();

        when(additionalRepository.findByNumberAndName(
                additionalEntity.getNumber(), additionalEntity.getName())).thenReturn(null);

        when(additionalRepository.save(additionalEntity)).thenReturn(additionalEntity);

        AdditionalEntity result = chapterPartService.createAdditional(additionalEntity);

        assertNotNull(result);
        assertEquals(additionalEntity, result);
    }

    @Test
    void testPartNumberExistsWhenPartExists() {
        int numberToCheck = 1;
        Set<ChapterPartEntity> parts = new HashSet<>();
        ChapterPartEntity part = new ChapterPartEntity();
        part.setNumber(numberToCheck);
        parts.add(part);

        boolean result = chapterPartService.partNumberExists(numberToCheck, parts);

        assertTrue(result);
    }

    @Test
    void testPartNumberExistsWhenPartDoesNotExist() {
        int numberToCheck = 1;
        Set<ChapterPartEntity> parts = new HashSet<>();
        ChapterPartEntity part = new ChapterPartEntity();
        part.setNumber(2);
        parts.add(part);

        boolean result = chapterPartService.partNumberExists(numberToCheck, parts);

        assertFalse(result);
    }

    @Test
    void testGetAllPractices() {
        Set<ChapterPartEntity> chapters = Set.of(new ChapterPartEntity());

        when(chapterPartRepository.findAllByChapterId(1)).thenReturn(Optional.of(chapters));

        Set<ChapterPartEntity> result = chapterPartService.getAllPractices(1).orElse(null);

        assertNotNull(result);
        assertEquals(chapters, result);
    }

    @Test
    void testGetChapterPartById() {
        ChapterPartEntity chapterPartEntity = new ChapterPartEntity();
        chapterPartEntity.setId(1);

        when(chapterPartRepository.findById(1)).thenReturn(chapterPartEntity);

        ChapterPartEntity result = chapterPartService.getChapterPartById(1);

        assertEquals(1, result.getId());
    }

    @Test
    void testGetChapterPartNumber() {
        String key = "1-2.";

        int expectedPartNumber = 2;

        int result = chapterPartService.getChapterPartNumber(key);

        assertEquals(expectedPartNumber, result);
    }

}