package com.group.practic.service;

import com.group.practic.controller.SkillController;
import com.group.practic.dto.SkillDto;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.SkillEntity;
import com.group.practic.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;


    public List<SkillEntity> findAll() {
        return skillRepository.findAll();
    }

    public SkillEntity findByName(String name) {
        return skillRepository.findByName(name);
    }

    public SkillEntity save(SkillDto skillDto) {
        SkillEntity skillEntity = skillRepository.findByName(skillDto.getName());
        if (skillEntity == null) {
            skillEntity = new SkillEntity();
            skillEntity.setName(skillDto.getName());
            skillRepository.save(skillEntity);
            return skillEntity;
        }
        return skillEntity;
    }

    public SkillEntity delete(String name) {
        return skillRepository.deleteByName(name);
    }

    public SkillEntity addSkillToChapter(String name, long chapterId) {
        SkillEntity skillEntity = skillRepository.findByName(name);

        if (skillEntity != null) {
//            ChapterEntity chapterEntity = find
//            skillEntity.addChapter(chapterEntity);
            skillRepository.save(skillEntity);
            return skillEntity;
        }

        return null;
    }
}
