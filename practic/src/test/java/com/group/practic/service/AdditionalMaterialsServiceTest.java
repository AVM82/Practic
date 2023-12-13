package com.group.practic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.group.practic.PropertyLoader;
import com.group.practic.entity.AdditionalMaterialsEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.repository.AdditionalMaterialsRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        when(additionalMaterialsRepository.findByCourseAndNumber(
                additionalMaterialEntity.getCourse(), additionalMaterialEntity.getNumber())
        ).thenReturn(null);

        when(additionalMaterialsRepository.save(additionalMaterialEntity))
                .thenReturn(additionalMaterialEntity);

        AdditionalMaterialsEntity result =
                additionalMaterialsService.createOrUpdate(additionalMaterialEntity);
        assertNotNull(result);
        assertEquals(additionalMaterialEntity, result);
    }

    @Test
    void testUpdateExistingAdditionalMaterial() {
        AdditionalMaterialsEntity existingMaterial = new AdditionalMaterialsEntity();
        existingMaterial.setId(1L);

        when(additionalMaterialsRepository.findByCourseAndNumber(existingMaterial.getCourse(),
                existingMaterial.getNumber()))
                .thenReturn(existingMaterial);

        AdditionalMaterialsEntity updatedMaterial = new AdditionalMaterialsEntity();
        updatedMaterial.setId(existingMaterial.getId());
        updatedMaterial.setName("newMaterial");

        when(additionalMaterialsRepository.save(updatedMaterial))
                .thenReturn(updatedMaterial);

        AdditionalMaterialsEntity result =
                additionalMaterialsService.createOrUpdate(updatedMaterial);
        assertNotNull(result);
        assertEquals(updatedMaterial, result);
    }

    @Test
    void testGetAdditionalMaterialsWhenPropertiesAreInvalid() {
        Map<Object, Object> properties = new HashMap<>();
        properties.put("additional_part1", "value1");
        properties.put("additional_part2", "value2");

        when(mockPropertyLoader.getEntrySet()).thenReturn(properties.entrySet());
        when(mockPropertyLoader.getProperty("additional_part1")).thenReturn("item1");

        CourseEntity mockCourse = mock(CourseEntity.class);

        List<AdditionalMaterialsEntity> result = additionalMaterialsService
                .getAdditionalMaterials(mockCourse, mockPropertyLoader);

        assertTrue(result.isEmpty());
    }
}