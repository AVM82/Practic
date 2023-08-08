package com.group.practic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.group.practic.entity.SubSubChapterEntity;

@Repository
public interface SubSubChapterRepository extends JpaRepository <SubSubChapterEntity, Integer> {
}
