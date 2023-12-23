package com.group.practic.repository;

import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.CourseSettingsEntity;
import com.group.practic.entity.MentorEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseSettingsRepository extends JpaRepository<CourseSettingsEntity, Long> {

    Optional<CourseSettingsEntity> findByCourse(CourseEntity course);
}
