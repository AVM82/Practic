package com.group.practic.repository;

import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.GraduateEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StudentEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GraduateRepository extends JpaRepository<GraduateEntity, Long> {

    Optional<GraduateEntity> findByStudent(StudentEntity student);

    List<GraduateEntity> findAllByCourse(CourseEntity course);

    List<GraduateEntity> findAllByPerson(PersonEntity person);

}
