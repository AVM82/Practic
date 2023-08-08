package com.group.practic.repository;

import com.group.practic.entity.SubChapterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubChapterRepository extends JpaRepository<SubChapterEntity, Integer> {
}
