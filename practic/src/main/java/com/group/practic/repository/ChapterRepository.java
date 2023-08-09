package com.group.practic.repository;

import com.group.practic.entity.ChapterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepository extends JpaRepository<ChapterEntity, Long> {

}
