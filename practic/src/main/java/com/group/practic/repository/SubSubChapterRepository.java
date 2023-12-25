package com.group.practic.repository;

import com.group.practic.entity.SubChapterEntity;
import com.group.practic.entity.SubSubChapterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SubSubChapterRepository extends JpaRepository<SubSubChapterEntity, Long> {

    SubSubChapterEntity findBySubChapterAndNumber(SubChapterEntity subChapter, int number);

}
