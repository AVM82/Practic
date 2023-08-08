package com.group.practic.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.group.practic.entity.ChapterEntity;

@Repository
public interface ChapterRepository extends JpaRepository <ChapterEntity, Integer> {

}
