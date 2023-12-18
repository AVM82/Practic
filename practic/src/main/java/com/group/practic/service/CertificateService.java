package com.group.practic.service;

import com.group.practic.dto.CertificateDto;
import com.group.practic.entity.CourseSettingsEntity;
import com.group.practic.entity.StudentEntity;
import com.group.practic.repository.StudentRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertificateService {
    private static final String DIPLOMA = "Запит на отримання диплому";
    private static final String CERTIFICATE = "Запит на отримання сертифікату";
    private final StudentRepository studentRepository;
    private final EmailSenderService emailSenderService;
    private final CourseSettingsService courseSettingsService;

    public Optional<CertificateDto> getCertificateInfo(Long studentId) {
        return studentRepository.findById(studentId)
                .filter(s -> PersonService.me().getId() == s.getPerson().getId())
                .flatMap(this::mapToDto);
    }

    public Optional<CertificateDto> sendCertificateRequest(
            CertificateDto requestDto, Long studentId) {
        Optional<StudentEntity> studentById = studentRepository.findById(studentId);
        if (studentById.isEmpty()) {
            return Optional.empty();
        }
        StudentEntity student = studentById.get();
        if (PersonService.me().getId() != studentById.get().getPerson().getId()) {
            return Optional.empty();
        }
        Optional<CourseSettingsEntity> settingsByCourse =
                courseSettingsService.getByCourse(student.getCourse());
        if (settingsByCourse.isEmpty()) {
            return Optional.empty();
        }
        if (!student.getSkills().containsAll(requestDto.skills())) {
            return Optional.empty();
        }
        CourseSettingsEntity courseSettings = settingsByCourse.get();
        String title = student.getActiveChapterNumber() == 0 ? DIPLOMA : CERTIFICATE;
        emailSenderService.sendEmail(
                courseSettings.getModeratorEmail(), title, generateMessage(requestDto));
        return Optional.of(requestDto);
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

    public String generateMessage(CertificateDto certificateDto) {
        return "Імʼя студента: " + certificateDto.studentName()
                + System.lineSeparator()
                + "Назва курсу: " + certificateDto.courseName()
                + System.lineSeparator()
                + "Дата вступу: " + certificateDto.start()
                + System.lineSeparator()
                + "Дата виипуску: " + certificateDto.finish()
                + System.lineSeparator()
                + "Днів навчання: " + certificateDto.daysSpent()
                + System.lineSeparator()
                + "Здобуті навички: " + certificateDto.skills()
                + System.lineSeparator()
                + "Електронна пошта: " + PersonService.me().getEmail()
                + System.lineSeparator()
                + "Сторінка студента: " + PersonService.me().getPersonPageUrl();
    }
}
