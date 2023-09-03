package com.group.practic.repository;

import com.group.practic.entity.CourseEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {

    Optional<CourseEntity> findByName(String name);

    Optional<CourseEntity> findBySlug(String slug);

    Optional<CourseEntity> findByShortName(String shortName);

}