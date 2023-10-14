package com.group.practic.service;

import com.group.practic.dto.ReferenceTitleDto;
import com.group.practic.entity.ReferenceTitleEntity;
import com.group.practic.repository.ReferenceTitleRepository;
import com.group.practic.util.FunctionThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
class ReferenceTitleServiceTest {

    @Mock
    private ReferenceTitleRepository referenceTitleRepository;

    @InjectMocks
    private ReferenceTitleService referenceTitleService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testGet() {
        ReferenceTitleEntity entity1 = new ReferenceTitleEntity();
        entity1.setId(1L);
        entity1.setReference("Reference1");
        entity1.setTitle("Title1");

        ReferenceTitleEntity entity2 = new ReferenceTitleEntity();
        entity2.setId(2L);
        entity2.setReference("Reference2");
        entity2.setTitle("Title2");

        Set<ReferenceTitleEntity> entities = new HashSet<>();
        entities.add(entity1);
        entities.add(entity2);
        when(referenceTitleRepository.findAll()).thenReturn(new ArrayList<>(entities)); // Перетворюємо Set на List
        Set<ReferenceTitleEntity> result = referenceTitleService.get();
        assertEquals(entities, result);
    }

    @Test
    void testCreate() {
        String reference = "TestReference";
        String title = "TestTitle";
        ReferenceTitleEntity existingEntity = new ReferenceTitleEntity();
        existingEntity.setReference(reference);
        existingEntity.setTitle(title);
        when(referenceTitleRepository.findByReference(reference)).thenReturn(existingEntity); // Повертаємо існуючий запис
        ReferenceTitleEntity result = referenceTitleService.create(reference);
        assertNotNull(result);
        assertEquals(reference, result.getReference());
        assertEquals(title, result.getTitle());
    }

    @SuppressWarnings("checkstyle:VariableDeclarationUsageDistance")
    @Test
    void testGetTitle() {
        String reference = "TestReference";
        String expectedTitle = "TestTitle";
        ReferenceTitleRepository referenceTitleRepository = mock(ReferenceTitleRepository.class);
        ReferenceTitleService referenceTitleService = new ReferenceTitleService(referenceTitleRepository);
        ReferenceTitleEntity referenceTitleEntity = new ReferenceTitleEntity();
        referenceTitleEntity.setReference(reference);
        referenceTitleEntity.setTitle(expectedTitle);
        when(referenceTitleRepository.findByReference(reference)).thenReturn(referenceTitleEntity);
        String result = referenceTitleService.getTitle(reference);
        assertEquals(expectedTitle, result);
    }

    @Test
    void testGetTitleForNonExistentReference() {
        String reference = "NonExistentReference";
        ReferenceTitleRepository referenceTitleRepository = mock(ReferenceTitleRepository.class);
        ReferenceTitleService referenceTitleService = new ReferenceTitleService(referenceTitleRepository);
        when(referenceTitleRepository.findByReference(reference)).thenReturn(null);
        String result = referenceTitleService.getTitle(reference);
        assertNull(result);
    }

    @Test
    void testUpdateExistingReference() {
        String reference = "TestReference";
        String originalTitle = "OriginalTitle";
        String updatedTitle = "UpdatedTitle";
        ReferenceTitleRepository referenceTitleRepository = mock(ReferenceTitleRepository.class);
        ReferenceTitleService referenceTitleService = new ReferenceTitleService(referenceTitleRepository);
        ReferenceTitleEntity existingEntity = new ReferenceTitleEntity();
        existingEntity.setReference(reference);
        existingEntity.setTitle(originalTitle);
        ReferenceTitleDto updatedDto = new ReferenceTitleDto();
        updatedDto.setReference(reference);
        updatedDto.setTitle(updatedTitle);
        when(referenceTitleRepository.findByReference(reference)).thenReturn(existingEntity);
        when(referenceTitleRepository.save(existingEntity)).thenReturn(existingEntity);
        ReferenceTitleEntity result = referenceTitleService.update(updatedDto);
        assertEquals(updatedTitle, result.getTitle());
    }

    @Test
    void testUpdateNonExistentReference() {
        String reference = "NonExistentReference";
        String updatedTitle = "UpdatedTitle";
        ReferenceTitleRepository referenceTitleRepository = mock(ReferenceTitleRepository.class);
        ReferenceTitleService referenceTitleService = new ReferenceTitleService(referenceTitleRepository);
        ReferenceTitleDto updatedDto = new ReferenceTitleDto();
        updatedDto.setReference(reference);
        updatedDto.setTitle(updatedTitle);
        when(referenceTitleRepository.findByReference(reference)).thenReturn(null);
        when(referenceTitleRepository.save(any(ReferenceTitleEntity.class))).thenAnswer(
                invocation -> {
                    ReferenceTitleEntity savedEntity = invocation.getArgument(0);
                    savedEntity.setId(1L);
                    return savedEntity;
                }
        );
        ReferenceTitleEntity result = referenceTitleService.update(updatedDto);
        assertEquals(updatedTitle, result.getTitle());
    }

    @Test
    void testUpdateNonExistentReferenceWithEntity() {
        String reference = "NonExistentReference";
        String updatedTitle = "UpdatedTitle";
        ReferenceTitleRepository referenceTitleRepository = mock(ReferenceTitleRepository.class);
        ReferenceTitleService referenceTitleService = new ReferenceTitleService(referenceTitleRepository);
        when(referenceTitleRepository.findByReference(reference)).thenReturn(null);
        ReferenceTitleEntity updatedEntity = new ReferenceTitleEntity();
        updatedEntity.setReference(reference);
        updatedEntity.setTitle(updatedTitle);

        when(referenceTitleRepository.save(updatedEntity)).thenAnswer(
                invocation -> {
                    ReferenceTitleEntity savedEntity = invocation.getArgument(0);
                    savedEntity.setId(1L);
                    return savedEntity;
                }
        );
        ReferenceTitleEntity result = referenceTitleService.update(updatedEntity);
        assertEquals(updatedTitle, result.getTitle());
    }

    @Test
    void testUpdateListOfReferenceTitleDto() {
        // Arrange
        ReferenceTitleRepository referenceTitleRepository = mock(ReferenceTitleRepository.class);
        ReferenceTitleService referenceTitleService = new ReferenceTitleService(referenceTitleRepository);

        ReferenceTitleDto dto1 = new ReferenceTitleDto();
        dto1.setReference("Reference1");
        dto1.setTitle("UpdatedTitle1");

        ReferenceTitleDto dto2 = new ReferenceTitleDto();
        dto2.setReference("Reference2");
        dto2.setTitle("UpdatedTitle2");

        Set<ReferenceTitleDto> dtoList = new HashSet<>();
        dtoList.add(dto1);
        dtoList.add(dto2);

        ReferenceTitleEntity entity1 = new ReferenceTitleEntity();
        entity1.setReference("Reference1");
        entity1.setTitle("OriginalTitle1");

        ReferenceTitleEntity entity2 = new ReferenceTitleEntity();
        entity2.setReference("Reference2");
        entity2.setTitle("OriginalTitle2");

        when(referenceTitleRepository.findByReference("Reference1")).thenReturn(entity1);
        when(referenceTitleRepository.findByReference("Reference2")).thenReturn(entity2);

        when(referenceTitleRepository.save(any(ReferenceTitleEntity.class))).thenAnswer(
                invocation -> {
                    ReferenceTitleEntity savedEntity = invocation.getArgument(0);
                    return savedEntity;
                }
        );
        Set<ReferenceTitleEntity> result = referenceTitleService.update(dtoList);
        assertEquals(dtoList.size(), result.size());
        for (ReferenceTitleEntity entity : result) {
            if ("Reference1".equals(entity.getReference())) {
                assertEquals("UpdatedTitle1", entity.getTitle());
            } else if ("Reference2".equals(entity.getReference())) {
                assertEquals("UpdatedTitle2", entity.getTitle());
            }
        }
    }
    @Test
    void testCreate1() {
        // Arrange
        ReferenceTitleRepository referenceTitleRepository = mock(ReferenceTitleRepository.class);
        FunctionThreadPool<String, ReferenceTitleEntity> functionThreadPool = mock(FunctionThreadPool.class);
        ReferenceTitleService referenceTitleService = new ReferenceTitleService(referenceTitleRepository);
        referenceTitleService.functionThreadPool = functionThreadPool;

        Collection<String> references = Set.of("Reference1", "Reference2");

        Set<ReferenceTitleEntity> expectedEntities = new HashSet<>();
        expectedEntities.add(new ReferenceTitleEntity(1L, "Reference1", "Title1"));
        expectedEntities.add(new ReferenceTitleEntity(2L, "Reference2", "Title2"));

        when(functionThreadPool.execute(references)).thenReturn(expectedEntities);

        Set<ReferenceTitleEntity> result = referenceTitleService.create(references);

        assertEquals(expectedEntities, result);

        verify(functionThreadPool).execute(references);
    }
}