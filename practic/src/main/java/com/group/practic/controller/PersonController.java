package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.badRequest;
import static com.group.practic.util.ResponseUtils.getResponse;
import static com.group.practic.util.ResponseUtils.postResponse;

import com.group.practic.dto.ApplicantDto;
import com.group.practic.dto.PersonDto;
import com.group.practic.entity.RoleEntity;
import com.group.practic.service.ApplicantService;
import com.group.practic.service.CourseService;
import com.group.practic.service.PersonService;
import jakarta.validation.constraints.Min;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
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
        this.applicantService = applicantService;
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'COLLABORATOR')")
    public ResponseEntity<Collection<PersonDto>> get(@RequestParam(required = false) String name,
            @RequestParam(required = false) boolean inactive,
            @RequestParam(required = false) boolean ban) {
        if (name == null) {
            return getResponse(PersonDto.map(personService.get(inactive, ban)));
        }
        return getResponse(PersonDto.map(personService.get(name, inactive, ban)));
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COLLABORATOR')")
    public ResponseEntity<PersonDto> get(@Min(1) @PathVariable long id) {
        return getResponse(personService.get(id).map(PersonDto::map));
    }


    @PostMapping("/{id}/add/{newRole}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COLLABORATOR')")
    public ResponseEntity<PersonDto> addRole(@PathVariable long id,
            @PathVariable String newRole) {
        RoleEntity role = personService.getRole(newRole);
        return role == null ? badRequest()
                : postResponse(personService.get(id)
                        .map(person -> PersonDto.map(personService.addRole(person, role))));
    }


    @PostMapping("/{id}/remove/{newRole}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COLLABORATOR')")
    public ResponseEntity<PersonDto> removeRole(@PathVariable long id,
            @PathVariable String newRole) {
        RoleEntity role = personService.getRole(newRole);
        return role == null ? badRequest()
                : postResponse(personService.get(id)
                        .map(person -> PersonDto.map(personService.removeRole(person, role))));
    }


    @GetMapping("/me")
    public ResponseEntity<PersonDto> getCurrentUser() {
        return getResponse(PersonDto.map(PersonService.me()));
    }


    @GetMapping("/application/{id}")
    public ResponseEntity<ApplicantDto> isApplied(@PathVariable long id) {
        return getResponse(applicantService.get(id).map(ApplicantDto::map));
    }


    @PostMapping("/application/{slug}")
    public ResponseEntity<ApplicantDto> applicationForCourse(@PathVariable String slug) {
        return postResponse(courseService.get(slug).map(personService::createApplication));
    }
    
}
