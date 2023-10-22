package com.group.practic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.group.practic.PropertyLoader;
import com.group.practic.entity.AdditionalMaterialsEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.repository.AdditionalMaterialsRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class AdditionalMaterialsServiceTest {

    @InjectMocks
    private AdditionalMaterialsService additionalMaterialsService;

    @Mock
    private AdditionalMaterialsRepository additionalMaterialsRepository;

    @Mock
    private PropertyLoader mockPropertyLoader;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateNewAdditionalMaterial() {
        AdditionalMaterialsEntity additionalMaterialEntity = new AdditionalMaterialsEntity();
        when(additionalMaterialsRepository.findByCourseAndNumberAndName(
                additionalMaterialEntity.getCourse(), additionalMaterialEntity.getNumber(),
                additionalMaterialEntity.getName())).thenReturn(Optional.empty());

        when(additionalMaterialsRepository.save(additionalMaterialEntity))
                .thenReturn(additionalMaterialEntity);

        Optional<AdditionalMaterialsEntity> result =
                additionalMaterialsService.update(additionalMaterialEntity);
        assertTrue(result.isPresent());
        assertEquals(additionalMaterialEntity, result.get());
    }

    @Test
    void testUpdateExistingAdditionalMaterial() {
        AdditionalMaterialsEntity existingMaterial = new AdditionalMaterialsEntity();
        existingMaterial.setId(1L);

        when(additionalMaterialsRepository.findById(existingMaterial.getId()))
                .thenReturn(Optional.of(existingMaterial));

        AdditionalMaterialsEntity updatedMaterial = new AdditionalMaterialsEntity();
        updatedMaterial.setId(existingMaterial.getId());

        when(additionalMaterialsRepository.save(updatedMaterial))
                .thenReturn(updatedMaterial);

        Optional<AdditionalMaterialsEntity> result =
                additionalMaterialsService.update(updatedMaterial);
        assertTrue(result.isPresent());
        assertEquals(updatedMaterial, result.get());
    }

    @Test
    void testGetAdditionalMaterialsWhenPropertiesAreInvalid() {
        Map<Object, Object> properties = new HashMap<>();
        properties.put("additional_part1", "value1");
        properties.put("additional_part2", "value2");

        when(mockPropertyLoader.getEntrySet()).thenReturn(properties.entrySet());
        when(mockPropertyLoader.getProperty("additional_part1")).thenReturn("item1");

        CourseEntity mockCourse = mock(CourseEntity.class);

        Set<AdditionalMaterialsEntity> result = additionalMaterialsService
                .getAdditionalMaterials(mockCourse, mockPropertyLoader);

        assertTrue(result.isEmpty());
    }
}