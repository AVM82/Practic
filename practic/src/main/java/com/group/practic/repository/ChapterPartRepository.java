package com.group.practic.repository;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.ChapterPartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterPartRepository extends JpaRepository<ChapterPartEntity, Long> {

    ChapterPartEntity findByChapterAndNumber(ChapterEntity chapter, int number);

}
