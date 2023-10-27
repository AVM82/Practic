package com.group.practic.repository;

import com.group.practic.entity.ApplicantEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantRepository extends JpaRepository<ApplicantEntity, Long> {

    List<ApplicantEntity> findAllByIsApplied(boolean isApplied);

    Optional<ApplicantEntity> findByPersonAndCourse(PersonEntity person, CourseEntity course);

    List<ApplicantEntity> findAllByCourseAndIsApplied(CourseEntity course, boolean isApplied);

}
