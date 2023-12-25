package com.group.practic.repository;

import com.group.practic.entity.AdditionalEntity;
import com.group.practic.entity.ChapterPartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalRepository extends JpaRepository<AdditionalEntity, Long> {

    AdditionalEntity findByChapterPartAndNumber(ChapterPartEntity chapterPart,  int number);
}
