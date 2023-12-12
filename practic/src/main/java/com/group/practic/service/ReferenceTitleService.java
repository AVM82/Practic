package com.group.practic.service;

import com.group.practic.dto.ReferenceTitleDto;
import com.group.practic.entity.ReferenceTitleEntity;
import com.group.practic.repository.ReferenceTitleRepository;
import com.group.practic.util.FunctionThreadPool;
import com.group.practic.util.MetaInfo;
import com.group.practic.util.PropertyUtil;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ReferenceTitleService {

    ReferenceTitleRepository referenceTitleRepository;

    FunctionThreadPool<String, ReferenceTitleEntity> functionThreadPool = new FunctionThreadPool<>(
            this::create, 200);


    @Autowired
    public ReferenceTitleService(ReferenceTitleRepository referenceTitleRepository) {
        this.referenceTitleRepository = referenceTitleRepository;
    }


    public Set<ReferenceTitleEntity> get() {
        return new HashSet<>(referenceTitleRepository.findAll());
    }


    public String getTitle(String reference) {
        ReferenceTitleEntity referenceTitleEntity = referenceTitleRepository
                .findByReference(reference);
        return referenceTitleEntity == null ? null : referenceTitleEntity.getTitle();
    }


    public ReferenceTitleEntity create(String reference) {
        ReferenceTitleEntity referenceTitleEntity = referenceTitleRepository
                .findByReference(reference);
        if (referenceTitleEntity != null) {
            return referenceTitleEntity;
        }
        String title = MetaInfo.getTitle(reference);
        return title == null ? null
                : referenceTitleRepository.save(new ReferenceTitleEntity(0, reference, title));
    }


    public Set<ReferenceTitleEntity> create(Collection<String> references) {
        return new HashSet<>(functionThreadPool.execute(references));
    }


    public ReferenceTitleEntity update(ReferenceTitleEntity referenceTitle) {
        ReferenceTitleEntity referenceTitleEntity = referenceTitleRepository
                .findByReference(referenceTitle.getReference());
        return referenceTitleEntity == null
                || referenceTitleEntity.getId() == referenceTitle.getId()
                        ? referenceTitleRepository.save(referenceTitle)
                        : null;
    }


    public ReferenceTitleEntity update(ReferenceTitleDto referenceTitleDto) {
        ReferenceTitleEntity referenceTitleEntity = referenceTitleRepository
                .findByReference(referenceTitleDto.getReference());
        if (referenceTitleEntity != null) {
            if (!referenceTitleEntity.getTitle().equals(referenceTitleDto.getTitle())) {
                referenceTitleEntity.setTitle(referenceTitleDto.getTitle());
                return referenceTitleRepository.save(referenceTitleEntity);
            }
            return referenceTitleEntity;
        }
        return referenceTitleRepository.save(new ReferenceTitleEntity(0,
                referenceTitleDto.getReference(), referenceTitleDto.getTitle()));
    }


    public Set<ReferenceTitleEntity> update(Set<ReferenceTitleDto> referenceTitleDtoList) {
        Set<ReferenceTitleEntity> result = new HashSet<>();
        for (ReferenceTitleDto referenceTitleDto : referenceTitleDtoList) {
            result.add(update(referenceTitleDto));
        }
        return result;
    }


    public Set<ReferenceTitleEntity> getReferenceTitleEntitySet(String[] part) {
        return part.length == 1 ? new HashSet<>()
                : create(Set.of(part[1].split(PropertyUtil.REFERENCE_SEPARATOR)));
    }

}
