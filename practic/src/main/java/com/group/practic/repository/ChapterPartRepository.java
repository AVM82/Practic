package com.group.practic.repository;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.ChapterPartEntity;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterPartRepository extends JpaRepository<ChapterPartEntity, Long> {

    ChapterPartEntity findByChapterAndNumber(ChapterEntity chapter, int number);

    Optional<Set<ChapterPartEntity>> findAllByChapterId(long chapterId);

    ChapterPartEntity findById(long chapterPartId);

}
