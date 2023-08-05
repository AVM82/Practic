package com.group.practic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.group.practic.dto.StudentDTO;
import com.group.practic.entity.StudentEntity;
import com.group.practic.repository.StudentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<StudentEntity> findAll() {
        return studentRepository.findAll();
    }

    public StudentEntity save(StudentDTO studentDTO) {
        return studentRepository.save(new StudentEntity(studentDTO.pib(), studentDTO.notes(), studentDTO.email(),
                studentDTO.phone(), studentDTO.discordId()));
    }
}
