package com.group.practic.repository;

import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.MentorEntity;
import com.group.practic.entity.PersonEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MentorRepository extends JpaRepository<MentorEntity, Long> {

    List<MentorEntity> findAllByPerson(PersonEntity person);

    MentorEntity findByPersonAndCourse(PersonEntity person, CourseEntity course);

}
