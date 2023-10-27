package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.badRequest;
import static com.group.practic.util.ResponseUtils.getResponse;

import com.group.practic.dto.MentorPracticeDto;
import com.group.practic.dto.SendMessageDto;
import com.group.practic.entity.ApplicantEntity;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.ChapterPartEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StudentEntity;
import com.group.practic.entity.StudentPracticeEntity;
import com.group.practic.enumeration.PracticeState;
import com.group.practic.service.ApplicantService;
import com.group.practic.service.ChapterPartService;
import com.group.practic.service.ChapterService;
import com.group.practic.service.CourseService;
import com.group.practic.service.EmailSenderService;
import com.group.practic.service.MentorService;
import com.group.practic.service.PersonService;
import com.group.practic.service.StudentService;
import com.group.practic.service.StudentPracticeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mentor")
public class MentorController {

    CourseService courseService;

    ApplicantService applicantService;

    MentorService mentorService;

    StudentPracticeService studentPracticeService;


    @Autowired
    public MentorController(MentorService mentorService, CourseService courseService,
            ApplicantService applicantService, StudentPracticeService studentPracticeService) {
        this.courseService = courseService;
        this.applicantService = applicantService;
        this.mentorService = mentorService;
        this.studentPracticeService = studentPracticeService;
    }


    @GetMapping("/applicants/{slug}")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<Collection<ApplicantEntity>> getApplicants(@PathVariable String slug) {
        Optional<CourseEntity> course = courseService.get(slug);
        return course.isEmpty() ? badRequest()
                : getResponse(applicantService.get(course.get(), false));
    }


    @PostMapping("/applicants/admit/{id}")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<Boolean> adminStudent(@Min(1) @PathVariable long id) {
        Optional<ApplicantEntity> applicant = applicantService.get(id);
        return applicant.isEmpty() ? badRequest()
                : getResponse(mentorService.adminStudent(applicant.get()));
    }


    @PostMapping("/applicants/reject/{id}")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<Boolean> rejectApplicant(@Min(1) @PathVariable long id) {
        Optional<ApplicantEntity> applicant = applicantService.get(id);
        return applicant.isEmpty() ? badRequest()
                : getResponse(mentorService.rejectApplicant(applicant.get()));
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
     * }
     */
    private boolean isAllPracticesApproved(Set<StudentPracticeEntity> practices) {
        return practices.stream()
                .allMatch(practice -> practice.getState() == PracticeState.APPROVED);
    }



}
