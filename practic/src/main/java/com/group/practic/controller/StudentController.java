package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.badRequest;
import static com.group.practic.util.ResponseUtils.deleteResponse;
import static com.group.practic.util.ResponseUtils.getResponse;
import static com.group.practic.util.ResponseUtils.postResponse;
import static com.group.practic.util.ResponseUtils.updateResponse;

import com.group.practic.dto.AdditionalMaterialsDto;
import com.group.practic.dto.ChapterCompleteDto;
import com.group.practic.dto.ChapterDto;
import com.group.practic.dto.NewStateChapterDto;
import com.group.practic.dto.PracticeDto;
import com.group.practic.dto.StudentReportCreationDto;
import com.group.practic.dto.StudentReportDto;
import com.group.practic.dto.TopicReportDto;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StudentChapterEntity;
import com.group.practic.entity.StudentEntity;
import com.group.practic.entity.StudentPracticeEntity;
import com.group.practic.entity.StudentReportEntity;
import com.group.practic.entity.TimeSlotEntity;
import com.group.practic.enumeration.ChapterState;
import com.group.practic.enumeration.PracticeState;
import com.group.practic.enumeration.ReportState;
import com.group.practic.service.AdditionalMaterialsService;
import com.group.practic.service.CourseService;
import com.group.practic.service.PersonService;
import com.group.practic.service.StudentReportService;
import com.group.practic.service.StudentService;
import com.group.practic.service.TimeSlotService;
import com.group.practic.service.TopicReportService;
import com.group.practic.util.Converter;
import com.group.practic.util.ResponseUtils;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    private final StudentReportService studentReportService;

    private final TimeSlotService timeSlotService;

    private final CourseService courseService;

    private final TopicReportService reportService;

    private final AdditionalMaterialsService additionalMaterialsService;

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
                .map(student -> studentService.getOpenedChapter(student, number).orElse(null)));
    }

    @GetMapping("/chapters/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Collection<ChapterDto>> getOpenChapters(@PathVariable long studentId) {
        return getResponse(studentService.get(studentId).map(studentService::getChapters));
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
            @PathVariable long subChapterId, @PathVariable boolean state) {
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

    @GetMapping("/reports/states")
    public ResponseEntity<Collection<String>> getReportStates() {
        List<String> reportStates = Arrays.stream(ReportState.values())
                .map(state -> state.name().toLowerCase()).toList();
        return getResponse(reportStates);
    }

    @GetMapping("/reports/course/{slug}")
    public ResponseEntity<Collection<List<StudentReportDto>>> getActualStudentReports(
            @PathVariable String slug) {
        return getResponse(Converter
                .convertListOfLists(studentReportService.getAllStudentsActualReports(slug)));

    }

    @PostMapping("/reports/{studentChapterId}")
    public ResponseEntity<StudentReportDto> postStudentReport(@PathVariable long studentChapterId,
            @RequestBody StudentReportCreationDto studentReportCreationDto) {
        return postResponse(studentService
                .getStudentChapter(studentChapterId).map(studentChapter -> studentReportService
                        .createStudentReport(studentChapter, studentReportCreationDto))
                .map(StudentReportDto::map));
    }

    @GetMapping("/reports/course/{slug}/timeslots")
    public ResponseEntity<Map<String, List<TimeSlotEntity>>> getAvailableTimeSlots(
            @PathVariable String slug) {
        return getResponse(Optional.ofNullable(timeSlotService.getAvailableTimeSlots()));
    }

    @PostMapping("/reports/course/{slug}/timeslots")
    public ResponseEntity<Optional<List<TimeSlotEntity>>> createTimeslots(
            @PathVariable String slug) {
        return postResponse(Optional.ofNullable(timeSlotService.fillTimeSlots()));
    }

    @PutMapping("/reports/likes/")
    public ResponseEntity<StudentReportDto> changeLikeCount(@RequestBody int reportId,
            Principal principal) {
        List<PersonEntity> personEntity = personService.get(principal.getName());
        if (!personEntity.isEmpty()) {
            Optional<StudentReportEntity> reportEntity = studentReportService
                    .changeReportLikeList(reportId, personEntity.get(0).getId());
            if (reportEntity.isPresent()) {
                return updateResponse(Optional.of(Converter.convert(reportEntity.get())));
            }
        }
        return ResponseUtils.notAcceptable();
    }

    @PutMapping("/reports/course/")
    public ResponseEntity<StudentReportDto> putStudentReport(
            @RequestBody StudentReportCreationDto studentReportCreationDto) {
        Optional<StudentReportEntity> reportEntity =
                studentReportService.changeReport(studentReportCreationDto);
        return reportEntity.isPresent()
                ? updateResponse(Optional.of(Converter.convert(reportEntity.get())))
                : updateResponse(Optional.empty());
    }

    @DeleteMapping("/reports/course/{reportId}")
    public ResponseEntity<StudentReportDto> deleteStudentReport(@PathVariable Integer reportId) {
        Optional<StudentReportEntity> reportEntity = studentReportService.deleteReport(reportId);
        return reportEntity.isPresent()
                ? deleteResponse(Optional.of(Converter.convert(reportEntity.get())))
                : deleteResponse(Optional.empty());
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
            @PathVariable long id, @RequestBody boolean state) {
        return studentService.get(studentId)
                .map(student -> updateResponse(additionalMaterialsService.get(id)
                        .map(add -> studentService.changeAdditionalMaterial(student, add, state))
                        .orElse(Optional.of(false))))
                .orElse(badRequest());
    }

    @GetMapping("/topicsreports/{studentChapterId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Collection<TopicReportDto>> getTopicsByChapter(
            @PathVariable Long studentChapterId) {

        Optional<StudentChapterEntity> chapter = studentService.getStudentChapter(studentChapterId);
        return chapter.isEmpty() ? badRequest()
                : getResponse(reportService.getTopicsByChapter(chapter.get().getChapter()).stream()
                        .map(TopicReportDto::new).toList());
    }
}
