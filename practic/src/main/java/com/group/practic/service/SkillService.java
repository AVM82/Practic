package com.group.practic.service;

import com.group.practic.dto.SkillDto;
import com.group.practic.entity.SkillEntity;
import com.group.practic.entity.SubChapterEntity;
import com.group.practic.repository.SkillRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;


@Service
public class SkillService {

    final SkillRepository skillRepository;

    final ChapterPartService chapterPartService;


    public SkillService(SkillRepository skillRepository, ChapterPartService chapterPartService) {
        this.skillRepository = skillRepository;
        this.chapterPartService = chapterPartService;
    }


    public List<SkillEntity> findAll() {
        return skillRepository.findAll();
    }


    public Optional<SkillEntity> findByName(String name) {
        return skillRepository.findByName(name);
    }


    public Optional<SkillEntity> save(SkillDto skillDto) {
        Optional<SkillEntity> skillEntityOptional = skillRepository.findByName(skillDto.getName());
        if (skillEntityOptional.isEmpty()) {
            return Optional.of(skillRepository.save(new SkillEntity(skillDto.getName())));
        }
        return Optional.empty();
    }


    public Optional<SkillEntity> delete(String name) {
        return skillRepository.deleteByName(name);
    }


    public Optional<SkillEntity> addSkillToChapter(String name, long subChapterId) {
        Optional<SkillEntity> skillEntityOptional = skillRepository.findByName(name);
        Optional<SubChapterEntity> subChapterEntityOptional = chapterPartService
                .getSub(subChapterId);
        if (skillEntityOptional.isPresent() && subChapterEntityOptional.isPresent()) {
            SkillEntity skillEntity = skillEntityOptional.get();
            SubChapterEntity subChapter = subChapterEntityOptional.get();
            skillEntity.addChapter(subChapter);
            return Optional.of(skillRepository.save(skillEntity));
        }

        return Optional.empty();
    }


    public Optional<SkillEntity> findById(long id) {
        return skillRepository.findById(id);
    }

}
