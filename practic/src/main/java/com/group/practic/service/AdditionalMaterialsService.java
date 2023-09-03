package com.group.practic.service;

import com.group.practic.entity.AdditionalMaterialsEntity;
import com.group.practic.repository.AdditionalMaterialsRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AdditionalMaterialsService {

    AdditionalMaterialsRepository additionalMaterialsRepository;


    @Autowired
    public AdditionalMaterialsService(AdditionalMaterialsRepository additionalMaterialsRepository) {
        this.additionalMaterialsRepository = additionalMaterialsRepository;
    }


    public List<AdditionalMaterialsEntity> get() {
        return additionalMaterialsRepository.findAll();
    }


    public Optional<AdditionalMaterialsEntity> get(String name) {
        return additionalMaterialsRepository.findByName(name);
    }


    public Optional<AdditionalMaterialsEntity> update(
            AdditionalMaterialsEntity additionalMaterialEntity) {
        Optional<AdditionalMaterialsEntity> result = additionalMaterialEntity.getId() == 0
                ? additionalMaterialsRepository.findByName(additionalMaterialEntity.getName())
                : additionalMaterialsRepository.findById(additionalMaterialEntity.getId());
        additionalMaterialEntity.setId(result.isPresent() ? result.get().getId() : 0);
        return Optional.ofNullable(additionalMaterialsRepository.save(additionalMaterialEntity));
    }

}
