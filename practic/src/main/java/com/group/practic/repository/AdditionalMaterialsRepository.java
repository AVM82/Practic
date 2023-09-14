package com.group.practic.repository;

import com.group.practic.entity.AdditionalMaterialsEntity;
import com.group.practic.entity.CourseEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AdditionalMaterialsRepository
        extends JpaRepository<AdditionalMaterialsEntity, Long> {

    Optional<AdditionalMaterialsEntity> findByCourseAndNumberAndName(CourseEntity course,
            int number, String name);

}
