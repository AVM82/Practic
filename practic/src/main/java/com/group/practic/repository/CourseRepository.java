package com.group.practic.repository;

import com.group.practic.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {

    CourseEntity findByName(String courseName);
}
