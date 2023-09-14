package com.group.practic.service;

import com.group.practic.PropertyLoader;
import com.group.practic.entity.AdditionalMaterialsEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.repository.AdditionalMaterialsRepository;
import com.group.practic.util.PropertyUtil;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AdditionalMaterialsService {

    AdditionalMaterialsRepository additionalMaterialsRepository;

    ReferenceTitleService referenceTitleService;


    @Autowired
    public AdditionalMaterialsService(AdditionalMaterialsRepository additionalMaterialsRepository,
            ReferenceTitleService referenceTitleService) {
        this.additionalMaterialsRepository = additionalMaterialsRepository;
        this.referenceTitleService = referenceTitleService;
    }


    public Optional<AdditionalMaterialsEntity> update(
            AdditionalMaterialsEntity additionalMaterialEntity) {
        Optional<AdditionalMaterialsEntity> result = additionalMaterialEntity.getId() == 0
                ? additionalMaterialsRepository.findByCourseAndNumberAndName(
                        additionalMaterialEntity.getCourse(), additionalMaterialEntity.getNumber(),
                        additionalMaterialEntity.getName())
                : additionalMaterialsRepository.findById(additionalMaterialEntity.getId());
        additionalMaterialEntity.setId(result.isPresent() ? result.get().getId() : 0);
        return Optional.ofNullable(additionalMaterialsRepository.save(additionalMaterialEntity));
    }


    Set<AdditionalMaterialsEntity> getAdditionalMaterials(CourseEntity course,
            PropertyLoader prop) {
        int n = 0;
        int max = 0;
        int current;
        for (Entry<Object, Object> entry : prop.getEntrySet()) {
            String key = (String) entry.getKey();
            if (PropertyUtil.keyStartsWith(key, PropertyUtil.ADDITIONAL_PART)) {
                n++;
                if (max < (current = PropertyUtil.getChapterNumber(2, key))) {
                    max = current;
                }
            }
        }
        if (n != max) {
            return Set.of();
        }
        Set<AdditionalMaterialsEntity> result = new HashSet<>(max);
        for (int i = 1; i <= max; i++) {
            String item = prop.getProperty(PropertyUtil.ADDITIONAL_PART + i);
            if (item == null) {
                break;
            }
            String[] part = item.split(PropertyUtil.NAME_SEPARATOR);
            Optional<AdditionalMaterialsEntity> additionalMaterial = update(
                    new AdditionalMaterialsEntity(0, course, i, part[0],
                            referenceTitleService.getReferenceTitleEntitySet(part)));
            if (additionalMaterial.isPresent()) {
                result.add(additionalMaterial.get());
            }
        }
        return result;
    }

}
