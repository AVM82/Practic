package com.group.practic.repository;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ChapterRepository extends JpaRepository<ChapterEntity, Long> {

    List<ChapterEntity> findAllByCourseOrderByNumberAsc(CourseEntity course);

    Optional<ChapterEntity> findByShortName(String shortName);

    Optional<ChapterEntity> findByCourseAndNumber(CourseEntity course, int number);
}
