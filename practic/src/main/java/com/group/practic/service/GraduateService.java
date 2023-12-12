package com.group.practic.service;

import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.GraduateEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StudentEntity;
import com.group.practic.repository.GraduateRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;


public class GraduateService {

    GraduateRepository graduateRepository;

    PersonService personService;

    EmailSenderService emailSenderService;


    @Autowired
    public GraduateService(GraduateRepository graduateRepository, PersonService personService,
            EmailSenderService emailSenderService) {
        this.graduateRepository = graduateRepository;
        this.personService = personService;
        this.emailSenderService = emailSenderService;
    }


    Optional<GraduateEntity> get(long id) {
        return graduateRepository.findById(id);
    }


    Optional<GraduateEntity> get(StudentEntity student) {
        return graduateRepository.findByStudent(student);
    }


    List<GraduateEntity> get(CourseEntity course) {
        return graduateRepository.findAllByCourse(course);
    }


    List<GraduateEntity> get(PersonEntity person) {
        return graduateRepository.findAllByPerson(person);
    }


    GraduateEntity create(StudentEntity student) {
        GraduateEntity graduate = graduateRepository.save(new GraduateEntity(student));
        personService.addGraduateRole(graduate);
        // PRAC-58 get certificate
        this.emailSenderService.sendEmail(student.getPerson().getEmail(), "Сертифікат",
                "Вітаємо ! Ось Вам сертифікат про закінчення курсу \""
                        + student.getCourse().getName() + "\"");
        return graduate;
    }

}
