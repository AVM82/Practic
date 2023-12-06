package com.group.practic.service;

import com.group.practic.PropertyLoader;
import com.group.practic.entity.AdditionalMaterialsEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.repository.AdditionalMaterialsRepository;
import com.group.practic.util.PropertyUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
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


    public Optional<AdditionalMaterialsEntity> get(long id) {
        return additionalMaterialsRepository.findById(id);
    }


    public AdditionalMaterialsEntity createOrUpdate(AdditionalMaterialsEntity newEntity) {
        AdditionalMaterialsEntity additionalMaterial = additionalMaterialsRepository
                .findByCourseAndNumber(newEntity.getCourse(), newEntity.getNumber());
        if (additionalMaterial == null) {
            additionalMaterialsRepository.save(newEntity);
        }
        return additionalMaterial.equals(newEntity) ? additionalMaterial
                : additionalMaterialsRepository.save(additionalMaterial.update(newEntity));
    }


    List<AdditionalMaterialsEntity> getAdditionalMaterials(CourseEntity course,
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
            return List.of();
        }
        List<AdditionalMaterialsEntity> result = new ArrayList<>(max);
        for (int i = 1; i <= max; i++) {
            String item = prop.getProperty(PropertyUtil.ADDITIONAL_PART + i);
            if (item == null) {
                break;
            }
            String[] part = item.split(PropertyUtil.NAME_SEPARATOR);
            AdditionalMaterialsEntity additionalMaterial =
                    createOrUpdate(new AdditionalMaterialsEntity(0, course, i, part[0],
                            referenceTitleService.getReferenceTitleEntitySet(part)));
            result.add(additionalMaterial);
        }
        return result;
    }

}
