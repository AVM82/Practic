package com.group.practic.service;

import com.group.practic.dto.CertificateDto;
import com.group.practic.entity.StudentEntity;
import com.group.practic.repository.StudentRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertificateService {
    private final StudentRepository studentRepository;

    public Optional<CertificateDto> getCertificateInfo(Long studentId) {
        return studentRepository.findById(studentId).flatMap(this::mapToDto);
    }

    public Optional<CertificateDto> mapToDto(StudentEntity student) {
        return Optional.of(new CertificateDto(
                student.getPerson().getName(),
                student.getCourse().getName(),
                student.getSkills(),
                student.getStart(),
                student.getFinish(),
                student.getDaysSpent()));
    }
}
