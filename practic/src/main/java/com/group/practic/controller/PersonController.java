package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.badRequest;
import static com.group.practic.util.ResponseUtils.getResponse;
import static com.group.practic.util.ResponseUtils.notAcceptable;
import static com.group.practic.util.ResponseUtils.postResponse;

import com.group.practic.dto.PersonDto;
import com.group.practic.entity.ApplicantEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.RoleEntity;
import com.group.practic.service.ApplicantService;
import com.group.practic.service.CourseService;
import com.group.practic.service.PersonService;
import jakarta.validation.constraints.Min;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private PersonService personService;

    private ApplicantService applicantService;

    CourseService courseService;

    @Autowired
    public PersonController(PersonService personService, CourseService courseService,
            ApplicantService applicantService) {
        this.personService = personService;
        this.applicantService = applicantService;
        this.courseService = courseService;
    }


    @GetMapping("/")
    public ResponseEntity<Collection<PersonEntity>> get(@RequestParam(required = false) String name,
            @RequestParam(required = false) boolean inactive,
            @RequestParam(required = false) boolean ban) {
        if (name == null) {
            return getResponse(personService.get(inactive, ban));
        }
        return getResponse(personService.get(name, inactive, ban));
    }


    @GetMapping("/{id}")
    public ResponseEntity<PersonEntity> get(@Min(1) @PathVariable long id) {
        return getResponse(personService.get(id));
    }


    @PostMapping
    public ResponseEntity<PersonEntity> createPerson(@RequestBody PersonDto personDto) {
        return postResponse(personService.create(personDto));
    }



    @PostMapping("/roles/{id}/{newRole}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PersonEntity> addRole(@PathVariable long id, @PathVariable String newRole) {
        Optional<PersonEntity> person = personService.get(id);
        RoleEntity role = personService.getRole(newRole); 
        return person.isPresent() && role != null 
                ? postResponse(Optional.of(personService.addRolesToPerson(person.get(), Set.of(role))))
                : badRequest();
    }


    @GetMapping("/me")
    public ResponseEntity<PersonDto> getCurrentUser() {
        return getResponse(personService.getMeDto());
    }


    @GetMapping("/application/{slug}/{id}")
    public ResponseEntity<Boolean> isApplied(@PathVariable String slug, @PathVariable long id) {
        Optional<CourseEntity> course = courseService.get(slug);
        Optional<PersonEntity> person = personService.get(id);
        if (course.isEmpty() || person.isEmpty()) {
            return badRequest(); 
        }
        Optional<ApplicantEntity> applicant = applicantService.get(person.get(), course.get());
        return applicant.isPresent() ? getResponse(applicant.get().isApplied()) : notAcceptable();
    }


    @PostMapping("/application/{slug}")
    @PreAuthorize("hasRole()")
    public ResponseEntity<ApplicantEntity> applicationForCourse(@PathVariable String slug) {
        PersonEntity me = PersonService.me();
        Optional<CourseEntity> course = courseService.get(slug);
        return me == null || course.isEmpty() ? badRequest() 
                : postResponse(applicantService.create(me, course.get()));
    }

}
