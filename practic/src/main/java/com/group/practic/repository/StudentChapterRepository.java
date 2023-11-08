package com.group.practic.repository;

import com.group.practic.entity.StudentChapterEntity;
import com.group.practic.entity.StudentEntity;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentChapterRepository extends JpaRepository<StudentChapterEntity, Long> {


}
