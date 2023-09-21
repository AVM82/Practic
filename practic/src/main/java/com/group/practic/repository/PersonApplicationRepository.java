package com.group.practic.repository;

import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonApplicationEntity;
import com.group.practic.entity.PersonEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonApplicationRepository extends JpaRepository<PersonApplicationEntity, Long> {

    List<PersonApplicationEntity> findAllByIsApply(boolean isApply);

    PersonApplicationEntity findByPersonAndCourse(PersonEntity person, CourseEntity course);

}
