package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.badRequest;
import static com.group.practic.util.ResponseUtils.getResponse;

import com.group.practic.dto.ApplicantDto;
import com.group.practic.dto.ApplicantsForCourseDto;
import com.group.practic.dto.ChapterDto;
import com.group.practic.dto.MentorDto;
import com.group.practic.dto.ShortChapterDto;
import com.group.practic.dto.StudentDto;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.MentorEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.service.ApplicantService;
import com.group.practic.service.CourseService;
import com.group.practic.service.MentorService;
import com.group.practic.service.PersonService;
import com.group.practic.service.StudentPracticeService;
import jakarta.validation.constraints.Min;
import java.util.Collection;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/mentors")
public class MentorController {

    CourseService courseService;

    ApplicantService applicantService;

    MentorService mentorService;

    PersonService personService;

    StudentPracticeService studentPracticeService;


    @Autowired
    public MentorController(MentorService mentorService, CourseService courseService,
            ApplicantService applicantService, StudentPracticeService studentPracticeService,
            PersonService personService) {
        this.courseService = courseService;
        this.applicantService = applicantService;
        this.mentorService = mentorService;
        this.studentPracticeService = studentPracticeService;
        this.personService = personService;
    }


    @PostMapping("/add/{slug}/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COLLABORATOR')")
    public ResponseEntity<MentorDto> addMentor(@PathVariable String slug,
            @Min(1) @PathVariable long id) {
        Optional<CourseEntity> course = courseService.get(slug);
        Optional<PersonEntity> person = personService.get(id);
        return course.isEmpty() || person.isEmpty() ? badRequest()
                : getResponse(Optional.of(mentorService.addMentor(person.get(), course.get())));
    }


    @PostMapping("/remove/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'COLLABORATOR')")
    public ResponseEntity<Boolean> removeMentor(@Min(1) @PathVariable long id) {
        Optional<MentorEntity> mentor = mentorService.get(id);
        return mentor.isEmpty() ? badRequest()
                : getResponse(Optional.of(mentorService.removeMentor(mentor.get())));
    }


    @GetMapping("/applicants")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<Collection<ApplicantsForCourseDto>> getMyApplicants() {
        return getResponse(mentorService.getMyApplicants());
    }


    @GetMapping("/applicants/{slug}")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<Collection<ApplicantDto>> getMyApplicantsForCourse(
            @PathVariable String slug) {
        Optional<CourseEntity> course = courseService.get(slug);
        return course.isEmpty() ? badRequest()
                : getResponse(mentorService.getApplicantsForCourse(course.get()));
    }


    @PostMapping("/applicants/admit/{id}")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<StudentDto> adminStudent(@Min(1) @PathVariable long id) {
        return getResponse(
                applicantService.get(id).map(applicant -> mentorService.adminStudent(applicant)));
    }


    @PostMapping("/applicants/reject/{id}")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<ApplicantDto> rejectApplicant(@Min(1) @PathVariable long id) {
        return getResponse(applicantService.get(id)
                .map(applicant -> ApplicantDto.map(mentorService.rejectApplicant(applicant))));
    }


    @GetMapping("/chapters/{slug}")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<Collection<ShortChapterDto>> getChapters(@PathVariable String slug) {
        return getResponse(courseService.get(slug).filter(mentorService::isMyCourse)
                .map(mentorService::getChapters));
    }


    
    @GetMapping("/chapters/{slug}/{number}")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<ChapterDto> getChapter(@PathVariable String slug,
            @Min(1) @PathVariable int number) {
        return getResponse(courseService.get(slug).filter(mentorService::isMyCourse)
                .map(course -> mentorService.getChapter(course, number).orElseGet(null)));
    }
    
    
    
    /*
     * @PostMapping("/practices")
     * 
     * @PreAuthorize("hasRole('MENTOR')") public ResponseEntity<MentorPracticeDto> approvePractice(
     * 
     * @RequestBody @Valid MentorPracticeDto studentPracticeDto) { Optional<StudentEntity> student =
     * studentOnCourseService.get(studentPracticeDto.getStudentId()); if (student.isPresent()) {
     * ChapterPartEntity chapterPart =
     * chapterPartService.getChapterPartById(studentPracticeDto.getChapterPartId());
     * 
     * StudentPracticeEntity studentPractice = studentPracticeService .getPractice(student.get(),
     * studentPracticeDto.getChapterPartId());
     * 
     * studentPractice.setState(PracticeState.APPROVED);
     * 
     * StudentPracticeEntity updatedPractice = studentPracticeService.save(studentPractice);
     * 
     * Set<StudentPracticeEntity> practices = studentPracticeService
     * .getAllPracticesByChapter(student.get(), chapterPart.getChapter());
     * 
     * ChapterEntity nextChapter = getNextChapter(chapterPart.getChapter()); if
     * (isAllPracticesApproved(practices) && nextChapter != null) {
     * studentChapterService.addChapter(student.get().getId(), nextChapter.getId());
     * this.notify(student.get().getStudent(), nextChapter.getShortName()); }
     * 
     * return ResponseEntity.ok(MentorPracticeDto.map(updatedPractice)); } else { return
     * ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); }
     * 
     * } private boolean isAllPracticesApproved(Set<StudentPracticeEntity> practices) { return
     * practices.stream() .allMatch(practice -> practice.getState() == PracticeState.APPROVED); }
     * 
     */

}
