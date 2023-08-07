package com.group.practic.service;

import com.group.practic.dto.StudentDto;
import com.group.practic.entity.StudentEntity;
import com.group.practic.repository.StudentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<StudentEntity> findAll() {
        return studentRepository.findAll();
    }

    public StudentEntity save(StudentDto studentDto) {
        return studentRepository.save(
                new StudentEntity(studentDto.pib(), studentDto.notes(), studentDto.email(),
                studentDto.phone(), studentDto.discordId()));
    }
}
