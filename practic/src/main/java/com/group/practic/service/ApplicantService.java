package com.group.practic.service;

import com.group.practic.dto.ApplicantDto;
import com.group.practic.entity.ApplicantEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.repository.ApplicantRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicantService {

    private ApplicantRepository applicantRepository;


    @Autowired
    public ApplicantService(ApplicantRepository applicantRepository) {
        this.applicantRepository = applicantRepository;
    }


    public Optional<ApplicantEntity> get(long id) {
        return applicantRepository.findById(id);
    }


    public List<ApplicantEntity> get(CourseEntity course, boolean isApplied) {
        return applicantRepository.findAllByCourseAndIsApplied(course, isApplied);
    }


    public Optional<ApplicantEntity> get(PersonEntity person, CourseEntity course) {
        return applicantRepository.findByPersonAndCourse(person, course);
    }


    public Optional<ApplicantEntity> create(PersonEntity person, CourseEntity course) {
        return person.getMentors().stream().anyMatch(mentor -> mentor.getCourse().equals(course)) 
                || applicantRepository.findByPersonAndCourse(person, course).isPresent()
                        ? Optional.empty()
                        : Optional.of(applicantRepository.save(new ApplicantEntity(person, course)));
    }


    public ApplicantEntity apply(ApplicantEntity applicant) {
        return applicantRepository.save(applicant.apply());
    }


    public boolean reject(ApplicantEntity applicant) {
        long id = applicant.getId();
        applicantRepository.delete(applicant);
        return !applicantRepository.existsById(id);
    }


    public List<ApplicantDto> getNotAppliedApplicants(CourseEntity course) {
        return get(course, false).stream().map(ApplicantDto::map).toList();
    }


}
