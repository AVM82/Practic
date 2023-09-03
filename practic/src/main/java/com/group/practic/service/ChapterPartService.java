package com.group.practic.service;

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
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ChapterPartService {

    ChapterPartRepository chapterPartRepository;

    SubChapterRepository subChapterRepository;

    SubSubChapterRepository subSubChapterRepository;

    PraxisRepository praxisRepository;

    AdditionalRepository additionalRepository;


    @Autowired
    public ChapterPartService(ChapterPartRepository chapterPartRepository,
            SubChapterRepository subChapterRepository, AdditionalRepository additionalRepository,
            SubSubChapterRepository subSubChapterRepository, PraxisRepository praxisRepository) {
        this.chapterPartRepository = chapterPartRepository;
        this.subChapterRepository = subChapterRepository;
        this.subSubChapterRepository = subSubChapterRepository;
        this.praxisRepository = praxisRepository;
        this.additionalRepository = additionalRepository;
    }


    public Optional<SubChapterEntity> getSub(long id) {
        return subChapterRepository.findById(id);
    }


    public Optional<SubSubChapterEntity> getSubSub(long id) {
        return subSubChapterRepository.findById(id);
    }


    public ChapterPartEntity create(ChapterPartEntity chapterPartEntity) {
        ChapterPartEntity result = chapterPartRepository.findByChapterAndNumber(
                chapterPartEntity.getChapter(), chapterPartEntity.getNumber());
        return result != null && result.equals(chapterPartEntity) ? result
                : chapterPartRepository.save(chapterPartEntity);
    }


    public SubChapterEntity createSub(SubChapterEntity subChapterEntity) {
        SubChapterEntity result = subChapterRepository
                .findByNumberAndName(subChapterEntity.getNumber(), subChapterEntity.getName());
        return result != null && result.equals(subChapterEntity) ? result
                : subChapterRepository.save(subChapterEntity);
    }


    public SubSubChapterEntity createSubSub(SubSubChapterEntity subSubChapterEntity) {
        SubSubChapterEntity result = subSubChapterRepository.findByNumberAndName(
                subSubChapterEntity.getNumber(), subSubChapterEntity.getName());
        return result != null && result.equals(subSubChapterEntity) ? result
                : subSubChapterRepository.save(subSubChapterEntity);
    }


    public PraxisEntity createPraxis(PraxisEntity praxisEntity) {
        PraxisEntity result = praxisRepository.findByNumberAndName(praxisEntity.getNumber(),
                praxisEntity.getName());
        return result != null && result.equals(praxisEntity) ? result
                : praxisRepository.save(praxisEntity);
    }


    public AdditionalEntity createAdditional(AdditionalEntity additionalEntity) {
        AdditionalEntity result = additionalRepository
                .findByNumberAndName(additionalEntity.getNumber(), additionalEntity.getName());
        return result != null && result.equals(additionalEntity) ? result
                : additionalRepository.save(additionalEntity);
    }

}
