package com.group.practic.repository;

import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StudentEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;


@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long> {

    List<StudentEntity> findAllByInactiveAndBan(boolean inactive, boolean ban);

    Optional<StudentEntity> findByPersonAndCourseAndInactiveAndBan(PersonEntity personEntity,
            CourseEntity courseEntity, boolean inactive, boolean ban);

    List<StudentEntity> findAllByPersonAndInactiveAndBan(PersonEntity personEntity,
            boolean inactive, boolean ban);

    List<StudentEntity> findAllByCourseAndInactiveAndBanOrderByActiveChapterNumber(
            CourseEntity courseEntity, boolean inactive, boolean ban);

    List<StudentEntity> findAllByBanFalseAndInactiveFalseAndActiveChapterNumberEquals(
            int chapterNumber);

    Optional<StudentEntity> findByPersonAndCourse(PersonEntity person, CourseEntity course);

}
