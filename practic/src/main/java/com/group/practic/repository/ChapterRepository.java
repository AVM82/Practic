package com.group.practic.repository;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ChapterRepository extends JpaRepository<ChapterEntity, Long> {

    List<ChapterEntity> findAllByCourse(CourseEntity course);

    List<ChapterEntity> findAllByCourseAndShortName(CourseEntity course, String shortname);

    ChapterEntity findByShortName(String shortName);
}
