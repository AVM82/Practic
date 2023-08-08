package com.group.practic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.group.practic.entity.SubChapterEntity;

@Repository
public interface SubChapterRepository extends JpaRepository <SubChapterEntity, Integer> {
}
