package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.badRequest;
import static com.group.practic.util.ResponseUtils.getResponse;
import static com.group.practic.util.ResponseUtils.postResponse;
import static com.group.practic.util.ResponseUtils.updateResponse;

import com.group.practic.dto.AdditionalMaterialsDto;
import com.group.practic.dto.ChapterCompleteDto;
import com.group.practic.dto.ChapterDto;
import com.group.practic.dto.FrontReportDto;
import com.group.practic.dto.NewStateChapterDto;
import com.group.practic.dto.PracticeDto;
import com.group.practic.dto.ReportDto;
import com.group.practic.entity.AdditionalMaterialsEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.ReportEntity;
import com.group.practic.entity.StudentChapterEntity;
import com.group.practic.entity.StudentEntity;
import com.group.practic.entity.StudentPracticeEntity;
import com.group.practic.entity.TopicReportEntity;
import com.group.practic.enumeration.ChapterState;
import com.group.practic.enumeration.PracticeState;
import com.group.practic.enumeration.ReportState;
import com.group.practic.service.AdditionalMaterialsService;
import com.group.practic.service.ChapterService;
import com.group.practic.service.CourseService;
import com.group.practic.service.PersonService;
import com.group.practic.service.ReportService;
import com.group.practic.service.StudentService;
import com.group.practic.service.TopicReportService;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    private final PersonService personService;

    private final CourseService courseService;

    private final TopicReportService topicReportService;

    private final AdditionalMaterialsService additionalMaterialsService;

    private final ReportService reportService;

    private final ChapterService chapterService;


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<Collection<StudentEntity>> get(
            @RequestParam(required = false) Optional<Long> courseId,
            @RequestParam(required = false) Optional<Long> personId,
            @RequestParam(required = false) boolean inactive,
            @RequestParam(required = false) boolean ban) {
        Optional<CourseEntity> course;
        Optional<PersonEntity> person;
        if (courseId.isEmpty()) {
            if (personId.isEmpty()) {
                return getResponse(studentService.get(inactive, ban));
            }
            person = personService.get(personId.get());
            return person.isEmpty() ? badRequest()
                    : getResponse(studentService.getCoursesOfPerson(person.get(), inactive, ban));
        }
        if (personId.isEmpty()) {
            course = courseService.get(courseId.get());
            return course.isEmpty() ? badRequest()
                    : getResponse(studentService.getStudentsOfCourse(course.get(), inactive, ban));
        }
        person = personService.get(personId.get());
        course = courseService.get(courseId.get());
        return person.isEmpty() || course.isEmpty() ? badRequest()
                : getResponse(studentService.get(person.get(), course.get(), inactive, ban)
                .map(List::of).orElseGet(List::of));
    }


    @GetMapping("/chapters/{studentId}/{number}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ChapterCompleteDto> getChapter(@PathVariable long studentId,
                                                         @PathVariable int number) {
        return getResponse(studentService.get(studentId)
                .flatMap(student -> studentService.getOpenedChapter(student, number)));
    }


    @GetMapping("/chapters/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Collection<ChapterDto>> getOpenChapters(@PathVariable long studentId) {
        return getResponse(studentService.get(studentId).map(studentService::getChapters));
    }


    @GetMapping("/chapters/chapter/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ChapterCompleteDto> getChapterExt(@PathVariable long id) {
        return getResponse(
                studentService.getStudentChapter(id).flatMap(studentService::getOpenedChapter));
    }


    @PutMapping("/chapters/states/{id}/{newStateString}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<NewStateChapterDto> stateChapter(@PathVariable long id,
                                                           @PathVariable String newStateString) {
        ChapterState newState = ChapterState.fromString(newStateString);
        Optional<StudentChapterEntity> chapter = studentService.getStudentChapter(id);
        return chapter.isPresent()
                ? getResponse(studentService.changeState(chapter.get(), newState))
                : badRequest();
    }


    @PutMapping("/skills/{chapterId}/{subChapterId}/{state}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Boolean> reSetSkills(@PathVariable long chapterId,
                                               @PathVariable long subChapterId,
                                               @PathVariable boolean state) {
        return getResponse(studentService.getStudentChapter(chapterId)
                .map(chapter -> studentService.reSetSkills(chapter, subChapterId, state)));
    }


    @GetMapping("/practices/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<PracticeDto> getPractice(@PathVariable long id) {
        return getResponse(studentService.getPractice(id).map(PracticeDto::map));
    }


    @PutMapping("/practices/states/{id}/{newStateString}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<PracticeDto> statePractice(@PathVariable long id,
                                                     @PathVariable String newStateString) {
        PracticeState newState = PracticeState.fromString(newStateString);
        Optional<StudentPracticeEntity> practice = studentService.getPractice(id);
        return practice.isEmpty() || newState == PracticeState.APPROVED ? badRequest()
                : updateResponse(
                Optional.of(studentService.changePracticeState(practice.get(), newState)));
    }


    /* REPORTS */


    @GetMapping("/reports/{id}")
    public ResponseEntity<ReportDto> getReport(@PathVariable long id) {
        return getResponse(reportService.get(id).map(ReportDto::map));
    }


    @GetMapping("/reports/freeDates/{slug}/{fromDate}")
    public ResponseEntity<List<Boolean>> isFreeDate(@PathVariable String slug,
                                                    @PathVariable LocalDate fromDate) {
        return getResponse(courseService.get(slug)
                .map(course -> reportService.getFreeDaysFrom(course, fromDate)));
    }


    @PostMapping("/reports/{slug}")
    public ResponseEntity<ReportDto> create(@PathVariable String slug,
                                            @RequestBody FrontReportDto createDto) {
        Optional<CourseEntity> course = courseService.get(slug);
        Optional<PersonEntity> person = personService.get(createDto.getPersonId());
        Optional<TopicReportEntity> topic = topicReportService.get(createDto.getTopicReportId());
        if (createDto.getDate().isAfter(LocalDate.now(ZoneOffset.UTC)) && course.isPresent()
                && person.isPresent() && topic.isPresent()) {
            return postResponse(
                    ReportDto.map(createDto.getStudentChapterId() != 0
                            ? reportService.create(
                            studentService
                                    .getStudentChapter(createDto.getStudentChapterId()),
                            course.get(), person.get(), createDto.getDate(), topic.get())
                            : reportService.create(course.get(), person.get(), createDto.getDate(),
                            createDto.getChapterNumber(), topic.get())));
        }
        return badRequest();
    }


    @PutMapping("/reports/{id}")
    public ResponseEntity<ReportDto> update(@PathVariable long id,
                                            @RequestBody FrontReportDto updateDto) {
        Optional<ReportEntity> report = reportService.get(id);
        Optional<TopicReportEntity> topic = topicReportService.get(updateDto.getTopicReportId());
        return report.isPresent() && topic.isPresent()
                ? updateResponse(ReportDto.map(reportService.update(report.get(),
                updateDto.getChapterNumber(),
                studentService.getStudentChapter(updateDto.getStudentChapterId())
                        .orElse(null),
                updateDto.getDate(), topic.get())))
                : badRequest();
    }


    @GetMapping("/reports/{slug}/{number}")
    public ResponseEntity<Collection<ReportDto>> getChapterReports(@PathVariable String slug,
                                                                   @PathVariable int number) {
        return getResponse(courseService.get(slug)
                .map(course -> reportService.getChapterActual(course, LocalDate.now(), number))
                .map(ReportDto::map));
    }


    @PatchMapping("/reports/{id}/{newState}")
    public ResponseEntity<ReportDto> changeState(@PathVariable long id,
                                                 @PathVariable ReportState newState) {
        return updateResponse(
                reportService.get(id).map(report -> reportService.changeState(report, newState)));
    }


    @PatchMapping("/reports/{id}/like")
    public ResponseEntity<ReportDto> like(@PathVariable long id) {
        return updateResponse(reportService.get(id).map(reportService::like));
    }


    @GetMapping("/additionalMaterials/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Collection<AdditionalMaterialsDto>> getAdditionalMaterials(
            @PathVariable long studentId) {
        return getResponse(
                studentService.get(studentId).map(studentService::getAdditionalMaterials));
    }


    @PutMapping("/additionalMaterials/{studentId}/{id}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Boolean> changeAdditionalMaterial(@PathVariable long studentId,
                                                            @PathVariable long id,
                                                            @RequestBody boolean state) {
        Optional<StudentEntity> student = studentService.get(studentId);
        Optional<AdditionalMaterialsEntity> add = additionalMaterialsService.get(id);
        return updateResponse(student.isPresent() && add.isPresent()
                ? studentService.changeAdditionalMaterial(student.get(), add.get(), state)
                : Optional.empty());
    }

}
