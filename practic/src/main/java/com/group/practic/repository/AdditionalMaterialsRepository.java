package com.group.practic.repository;

import com.group.practic.entity.AdditionalMaterialsEntity;
import com.group.practic.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AdditionalMaterialsRepository
        extends JpaRepository<AdditionalMaterialsEntity, Long> {

    AdditionalMaterialsEntity findByCourseAndNumber(CourseEntity course, int number);

}
