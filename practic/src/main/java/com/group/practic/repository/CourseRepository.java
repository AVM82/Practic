package com.group.practic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.group.practic.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Course findByName(String courseName);
}
