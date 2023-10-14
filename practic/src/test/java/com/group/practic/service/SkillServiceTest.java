package com.group.practic.service;

import com.group.practic.dto.SkillDto;
import com.group.practic.entity.SkillEntity;
import com.group.practic.entity.SubChapterEntity;
import com.group.practic.repository.SkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class SkillServiceTest {

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private SkillService skillService;
    @Mock

    private ChapterPartService chapterPartService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testFindAll() {
        List<SkillEntity> skills = new ArrayList<>();
        SkillEntity skill1 = new SkillEntity();
        skill1.setId(1L);
        skill1.setName("Skill1");
        SkillEntity skill2 = new SkillEntity();
        skill2.setId(2L);
        skill2.setName("Skill2");
        skills.add(skill1);
        skills.add(skill2);

        when(skillRepository.findAll()).thenReturn(skills);

        List<SkillEntity> result = skillService.findAll();
        assertEquals(2, result.size());
        assertEquals("Skill1", result.get(0).getName());
        assertEquals("Skill2", result.get(1).getName());
    }

    @Test
    void testFindByName() {
        String skillName = "Skill1";
        SkillEntity skill = new SkillEntity();
        skill.setId(1L);
        skill.setName(skillName);
        when(skillRepository.findByName(skillName)).thenReturn(Optional.of(skill));
        Optional<SkillEntity> result = skillService.findByName(skillName);
        assertTrue(result.isPresent());
        assertEquals(skillName, result.get().getName());
    }

    @Test
    void testSave() {
        String skillName = "Skill1";
        SkillDto skillDto = new SkillDto();
        skillDto.setName(skillName);
        SkillEntity skill = new SkillEntity();
        skill.setName(skillName);

        when(skillRepository.findByName(skillName)).thenReturn(Optional.empty());
        when(skillRepository.save(skill)).thenReturn(skill);

        Optional<SkillEntity> result = skillService.save(skillDto);

        assertTrue(result.isPresent());
        assertEquals(skillName, result.get().getName());
    }

    @Test
    void testDelete() {
        String skillName = "SkillToDelete";
        SkillEntity skillToDelete = new SkillEntity();
        skillToDelete.setName(skillName);

        when(skillRepository.deleteByName(skillName)).thenReturn(Optional.of(skillToDelete));

        Optional<SkillEntity> result = skillService.delete(skillName);

        assertTrue(result.isPresent());
        assertEquals(skillName, result.get().getName());
    }

    @Test
    void testFindById() {
        long skillId = 1L;
        SkillEntity skill = new SkillEntity();
        skill.setId(skillId);
        skill.setName("Skill1");

        when(skillRepository.findById(skillId)).thenReturn(Optional.of(skill));

        Optional<SkillEntity> result = skillService.findById(skillId);

        assertTrue(result.isPresent());
        assertEquals(skillId, result.get().getId());
        assertEquals("Skill1", result.get().getName());
    }

    @Test
    void testAddSkillToChapter() {

        String skillName = "Skill1";
        long subChapterId = 1L;
        SkillEntity skill = new SkillEntity();
        skill.setId(1L);
        skill.setName(skillName);

        SubChapterEntity subChapter = new SubChapterEntity();
        subChapter.setId(subChapterId);

        when(skillRepository.findByName(skillName)).thenReturn(Optional.of(skill));
        when(chapterPartService.getSub(subChapterId)).thenReturn(Optional.of(subChapter));
        when(skillRepository.save(skill)).thenReturn(skill);

        Optional<SkillEntity> result = skillService.addSkillToChapter(skillName, subChapterId);
        assertTrue(result.isPresent());
        assertEquals(skill, result.get());
        assertTrue(result.get().getSubChapters().contains(subChapter));
    }
}