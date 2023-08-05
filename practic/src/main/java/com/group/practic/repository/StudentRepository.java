package com.group.practic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.group.practic.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Student findByPib(String studentPib);
}
