package com.group.practic.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.LevelEntity;

@Repository
public interface LevelRepository extends JpaRepository <LevelEntity, Integer> {

	List<LevelEntity> findAllByCourse(CourseEntity course);
}
