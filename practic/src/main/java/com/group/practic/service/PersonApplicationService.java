package com.group.practic.service;

import com.group.practic.dto.PersonApplyOnCourseDto;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonApplicationEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.exception.ResourceNotFoundException;
import com.group.practic.repository.CourseRepository;
import com.group.practic.repository.PersonApplicationRepository;
import com.group.practic.repository.PersonRepository;
import com.group.practic.util.Converter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonApplicationService {

    private final PersonApplicationRepository personApplicationRepository;

    private final PersonRepository personRepository;

    private final CourseRepository courseRepository;

    @Autowired
    public PersonApplicationService(PersonApplicationRepository personApplicationRepository,
            PersonRepository personRepository, CourseRepository courseRepository) {
        this.personApplicationRepository = personApplicationRepository;
        this.personRepository = personRepository;
        this.courseRepository = courseRepository;
    }


    public PersonEntity addPersonApplication(PersonEntity person, String courseSlug) {
        Set<CourseEntity> applyOnCourseSet = person.getCourses();
        Optional<CourseEntity> course = courseRepository.findBySlug(courseSlug);
        if (course.isPresent()) {
            applyOnCourseSet.add(course.get());
            person.setCourses(applyOnCourseSet);
            return personRepository.save(person);
        } else {
            throw new ResourceNotFoundException("Course", "slug", courseSlug);
        }
    }


    public List<PersonApplyOnCourseDto> getNotApplyPerson() {
        List<PersonApplicationEntity> applicants =
                personApplicationRepository.findAllByIsApply(false);
        return applicants.stream().map(Converter::convert).toList();
    }


    public PersonApplicationEntity getApplicationByPersonAndCourse(PersonEntity person,
            String courseSlug) {
        Optional<CourseEntity> course = courseRepository.findBySlug(courseSlug);
        if (course.isPresent()) {
            return personApplicationRepository.findByPersonAndCourse(person, course.get());
        } else {
            throw new ResourceNotFoundException("Course", "slug", courseSlug);
        }
    }


    public Optional<Boolean> amIwaiting(String slug, PersonEntity person) {
        Optional<CourseEntity> course = courseRepository.findBySlug(slug);
        if (person == null || course.isEmpty()) {
            return Optional.empty();
        }
        PersonApplicationEntity personApplication = 
                personApplicationRepository.findByPersonAndCourse(person, course.get());
        return Optional.of(personApplication != null && !personApplication.isApply());
    }

}
