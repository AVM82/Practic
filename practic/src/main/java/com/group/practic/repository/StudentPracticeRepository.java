package com.group.practic.repository;

import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.ChapterPartEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StudentPracticeEntity;
import com.group.practic.enumeration.PracticeState;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StudentPracticeRepository extends JpaRepository<StudentPracticeEntity, Long> {

    List<StudentPracticeEntity> findByState(PracticeState state);

    StudentPracticeEntity findByStudentAndChapterPart(
            PersonEntity student, ChapterPartEntity chapterPart);

    Set<StudentPracticeEntity> findByStudentAndChapter(
            PersonEntity student, ChapterEntity chapter
    );

}
