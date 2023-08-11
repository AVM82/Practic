package com.group.practic.repository;

import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StudentOnCourseEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StudentOnCourseRepository extends JpaRepository<StudentOnCourseEntity, Long> {

    List<StudentOnCourseEntity> findAllByInactiveAndBan(boolean inactive, boolean ban);

    List<StudentOnCourseEntity> findAllByCourseAndStudentAndInactiveAndBan(
            CourseEntity courseEntity, PersonEntity personEntity, boolean inactive, boolean ban);

    List<StudentOnCourseEntity> findAllByStudentAndInactiveAndBan(PersonEntity personEntity,
            boolean inactive, boolean ban);

    List<StudentOnCourseEntity> findAllByCourseAndInactiveAndBan(CourseEntity courseEntity,
            boolean inactive, boolean ban);

}
