package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.badRequest;
import static com.group.practic.util.ResponseUtils.getResponse;
import static com.group.practic.util.ResponseUtils.postResponse;

import com.group.practic.dto.ApplicantDto;
import com.group.practic.dto.PersonDto;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.RoleEntity;
import com.group.practic.entity.StateStudentEntity;
import com.group.practic.service.ApplicantService;
import com.group.practic.service.CourseService;
import com.group.practic.service.PersonService;
import jakarta.validation.constraints.Min;
import java.util.Collection;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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


    @PostMapping("/roles/{id}/{newRole}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PersonEntity> addRole(@PathVariable long id,
            @PathVariable String newRole) {
        Optional<PersonEntity> person = personService.get(id);
        RoleEntity role = personService.getRole(newRole);
        return person.isEmpty() || role == null ? badRequest()
                : postResponse(Optional.of(personService.addRole(person.get(), role)));
    }


    @GetMapping("/me")
    public ResponseEntity<PersonDto> getCurrentUser() {
        return getResponse(PersonDto.map(PersonService.me()));
    }


    @GetMapping("/application/{id}")
    public ResponseEntity<Boolean> isApplied(@PathVariable String slug, @PathVariable long id) {
        return applicantService.get(id).map(applicant -> getResponse(applicant.isApplied()))
                .orElse(badRequest());
    }


    @PostMapping(value = "/application/{slug}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<ApplicantDto> applicationForCourse(@PathVariable String slug) {
        Optional<CourseEntity> course = courseService.get(slug);
        if  (course.isPresent())  
            return  postResponse(personService.createApplication(course.get()));
        return badRequest();
    }

    
    @GetMapping("/students/{slug}")
    public ResponseEntity<StateStudentEntity> getStateStudent(@PathVariable String slug) {
        return getResponse(personService.getStateStudent(slug));
    }
    
}
