package com.group.practic.service;

import com.group.practic.PropertyLoader;
import com.group.practic.entity.AdditionalEntity;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.ChapterPartEntity;
import com.group.practic.entity.PraxisEntity;
import com.group.practic.entity.SubChapterEntity;
import com.group.practic.entity.SubSubChapterEntity;
import com.group.practic.repository.AdditionalRepository;
import com.group.practic.repository.ChapterPartRepository;
import com.group.practic.repository.PraxisRepository;
import com.group.practic.repository.SubChapterRepository;
import com.group.practic.repository.SubSubChapterRepository;
import com.group.practic.util.PropertyUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ChapterPartService {

    ChapterPartRepository chapterPartRepository;

    SubChapterRepository subChapterRepository;

    SubSubChapterRepository subSubChapterRepository;

    PraxisRepository praxisRepository;

    AdditionalRepository additionalRepository;

    ReferenceTitleService referenceTitleService;


    @Autowired
    public ChapterPartService(ChapterPartRepository chapterPartRepository,
            SubChapterRepository subChapterRepository, AdditionalRepository additionalRepository,
            SubSubChapterRepository subSubChapterRepository, PraxisRepository praxisRepository,
            ReferenceTitleService referenceTitleService) {
        this.chapterPartRepository = chapterPartRepository;
        this.subChapterRepository = subChapterRepository;
        this.subSubChapterRepository = subSubChapterRepository;
        this.praxisRepository = praxisRepository;
        this.additionalRepository = additionalRepository;
        this.referenceTitleService = referenceTitleService;
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
        SubChapterEntity result = subChapterRepository.findByChapterPartAndNumberAndName(
                subChapterEntity.getChapterPart(), subChapterEntity.getNumber(),
                subChapterEntity.getName());
        return result != null && result.equals(subChapterEntity) ? result
                : subChapterRepository.save(subChapterEntity);
    }


    public SubSubChapterEntity createSubSub(SubSubChapterEntity subSubChapterEntity) {
        SubSubChapterEntity result = subSubChapterRepository.findBySubChapterAndNumberAndName(
                subSubChapterEntity.getSubChapter(), subSubChapterEntity.getNumber(),
                subSubChapterEntity.getName());
        return result != null && result.equals(subSubChapterEntity) ? result
                : subSubChapterRepository.save(subSubChapterEntity);
    }


    public PraxisEntity createPraxis(PraxisEntity praxisEntity) {
        PraxisEntity result = praxisRepository.findByChapterPartAndNumberAndName(
                praxisEntity.getChapterPart(), praxisEntity.getNumber(), praxisEntity.getName());
        return result != null && result.equals(praxisEntity) ? result
                : praxisRepository.save(praxisEntity);
    }


    public AdditionalEntity createAdditional(AdditionalEntity additionalEntity) {
        AdditionalEntity result = additionalRepository
                .findByNumberAndName(additionalEntity.getNumber(), additionalEntity.getName());
        return result != null && result.equals(additionalEntity) ? result
                : additionalRepository.save(additionalEntity);
    }


    List<ChapterPartEntity> getChapterPartSet(ChapterEntity chapter, PropertyLoader prop) {
        List<ChapterPartEntity> result = new ArrayList<>();
        String keyStarts = PropertyUtil.createKeyStarts(chapter.getNumber(), "");
        for (Entry<Object, Object> entry : prop.getEntrySet()) {
            String key = (String) entry.getKey();
            if (key.startsWith(keyStarts) && PropertyUtil.countDots(key) == 2 && key.endsWith(".")
                    && PropertyUtil.getChapterNumber(2, key) != 0) {
                result.add(getChapterPart(chapter, 1, prop, keyStarts));
                break;
            }
        }
        keyStarts = String.valueOf(chapter.getNumber()) + PropertyUtil.PART_SEPARATOR;
        for (Entry<Object, Object> entry : prop.getEntrySet()) {
            String key = (String) entry.getKey();
            if (key.startsWith(keyStarts) && PropertyUtil.countDots(key) == 2 && key.endsWith(".")
                    && PropertyUtil.getChapterNumber(2, key) != 0) {
                int n = getChapterPartNumber(key);
                if (result.stream().allMatch(part -> part.getNumber() != n)) {
                    result.add(getChapterPart(chapter, n, prop,
                            key.substring(0, key.indexOf(PropertyUtil.DOT) + 1)));
                    break;
                }
            }
        }
        return result;
    }


    int getChapterPartNumber(String key) {
        return PropertyUtil.getNumber(key.substring(key.indexOf(PropertyUtil.PART_SEPARATOR) + 1,
                key.indexOf(PropertyUtil.DOT)));
    }


    ChapterPartEntity getChapterPart(ChapterEntity chapter, int number, PropertyLoader prop,
            String keyStarts) {
        String praxisKey = keyStarts + PropertyUtil.PRAXIS_PART;
        ChapterPartEntity chapterPart = create(
                new ChapterPartEntity(chapter, number, prop.getProperty(praxisKey, "-")));
        if (chapterPart != null) {
            getSubChapterSet(chapterPart, prop, keyStarts);
            getPraxisSet(chapterPart, prop, praxisKey);
            getAdditionalSet(chapterPart, prop, keyStarts + PropertyUtil.ADDITIONAL_PART);
        }
        return chapterPart;
    }


    List<SubChapterEntity> getSubChapterSet(ChapterPartEntity chapterPart, PropertyLoader prop,
            String keyStarts) {
        List<SubChapterEntity> result = new ArrayList<>();
        int n;
        for (Entry<Object, Object> entry : prop.getEntrySet()) {
            String key = (String) entry.getKey();
            if (key.startsWith(keyStarts) && PropertyUtil.countDots(key) == 2 && key.endsWith(".")
                    && (n = PropertyUtil.getChapterNumber(2, key)) != 0) {
                String[] nameRefs = ((String) entry.getValue()).split(PropertyUtil.NAME_SEPARATOR);
                SubChapterEntity subChapter = createSub(new SubChapterEntity(0, chapterPart, n,
                        nameRefs[0], referenceTitleService.getReferenceTitleEntitySet(nameRefs)));
                if (subChapter != null) {
                    subChapter.setSubSubChapters(getSubSubChapterSet(subChapter, prop, key));
                    result.add(subChapter);
                }
            }
        }
        return result;
    }


    List<SubSubChapterEntity> getSubSubChapterSet(SubChapterEntity subChapter, PropertyLoader prop,
            String keyStarts) {
        List<SubSubChapterEntity> result = new ArrayList<>();
        int n;
        for (Entry<Object, Object> entry : prop.getEntrySet()) {
            String key = (String) entry.getKey();
            if (key.startsWith(keyStarts) && key.endsWith(".")
                    && (n = PropertyUtil.getChapterNumber(3, key)) != 0) {
                String[] nameRefs = ((String) entry.getValue()).split(PropertyUtil.NAME_SEPARATOR);
                SubSubChapterEntity subSubChapter = createSubSub(
                        new SubSubChapterEntity(0, subChapter, n, nameRefs[0],
                                referenceTitleService.getReferenceTitleEntitySet(nameRefs)));
                if (subSubChapter != null) {
                    result.add(subSubChapter);
                }
            }
        }
        return result;
    }


    List<PraxisEntity> getPraxisSet(ChapterPartEntity chapterPart, PropertyLoader prop,
            String keyStarts) {
        List<PraxisEntity> result = new ArrayList<>();
        int n;
        for (Entry<Object, Object> entry : prop.getEntrySet()) {
            String key = (String) entry.getKey();
            if (key.startsWith(keyStarts) && (n = PropertyUtil.getChapterNumber(3, key)) != 0) {
                String[] nameRefs = ((String) entry.getValue()).split(PropertyUtil.NAME_SEPARATOR);
                PraxisEntity praxis = createPraxis(new PraxisEntity(0, chapterPart, n, nameRefs[0],
                        referenceTitleService.getReferenceTitleEntitySet(nameRefs)));
                if (praxis != null) {
                    result.add(praxis);
                }
            }
        }
        return result;
    }


    Set<AdditionalEntity> getAdditionalSet(ChapterPartEntity chapterPart, PropertyLoader prop,
            String keyStarts) {
        Set<AdditionalEntity> result = new HashSet<>();
        int n;
        for (Entry<Object, Object> entry : prop.getEntrySet()) {
            String key = (String) entry.getKey();
            if (key.startsWith(keyStarts) && (n = PropertyUtil.getChapterNumber(3, key)) != 0) {
                String[] nameRefs = ((String) entry.getValue()).split(PropertyUtil.NAME_SEPARATOR);
                AdditionalEntity additional = createAdditional(
                        new AdditionalEntity(0, chapterPart, n, nameRefs[0],
                                referenceTitleService.getReferenceTitleEntitySet(nameRefs)));
                if (additional != null) {
                    result.add(additional);
                }
            }
        }
        return result;
    }

    public Optional<Set<ChapterPartEntity>> getAllPractices(long chapterId) {
        return chapterPartRepository.findAllByChapterId(chapterId);
    }

    public ChapterPartEntity getChapterPartById(long chapterPartId) {
        return chapterPartRepository.findById(chapterPartId);
    }

}
