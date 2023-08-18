package com.group.practic.service;

import com.group.practic.entity.StudentPracticeEntity;
import com.group.practic.enumeration.PracticeState;
import com.group.practic.repository.StudentPracticeRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentPracticeService {

    private final StudentPracticeRepository studentPracticeRepository;

    @Autowired
    public StudentPracticeService(StudentPracticeRepository studentPracticeRepository) {
        this.studentPracticeRepository = studentPracticeRepository;
    }

    public List<StudentPracticeEntity> getAllStudentsByState(PracticeState state) {
        return studentPracticeRepository.findByState(state);
    }

}
