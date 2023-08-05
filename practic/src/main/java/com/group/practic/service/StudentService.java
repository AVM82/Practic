package com.group.practic.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.group.practic.dto.StudentDTO;
import com.group.practic.entity.Student;
import com.group.practic.repository.StudentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student save(StudentDTO studentDTO) {
        return studentRepository.save(new Student(studentDTO.pib(), studentDTO.notes(), studentDTO.email(),
                studentDTO.phone(), studentDTO.discordId()));
    }
}
