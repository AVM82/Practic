package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.badRequest;
import static com.group.practic.util.ResponseUtils.deleteResponse;
import static com.group.practic.util.ResponseUtils.getResponse;
import static com.group.practic.util.ResponseUtils.postResponse;
import static com.group.practic.util.ResponseUtils.updateResponse;

import com.group.practic.dto.AdditionalMaterialsDto;
import com.group.practic.dto.MentorPracticeDto;
import com.group.practic.dto.ShortChapterDto;
import com.group.practic.dto.StudentChapterDto;
import com.group.practic.dto.StudentPracticeDto;
import com.group.practic.dto.StudentReportCreationDto;
import com.group.practic.dto.StudentReportDto;
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
import com.group.practic.service.StudentService;
import com.group.practic.service.StudentPracticeService;
import com.group.practic.service.StudentReportService;
import com.group.practic.service.TimeSlotService;
import com.group.practic.util.Converter;
import com.group.practic.util.ResponseUtils;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
@RequestMapping("/api/students")
public class StudentController {

    private StudentService studentService;

    private final StudentPracticeService studentPracticeService;

    private final PersonService personService;

    private final StudentReportService studentReportService;

    private final TimeSlotService timeSlotService;

    private final CourseService courseService;

    AdditionalMaterialsService additionalMaterialsService;


    @Autowired
    public StudentController(StudentService studentService,
            StudentPracticeService studentPracticeService, PersonService personService,
            StudentReportService studentReportService, TimeSlotService timeSlotService,
            CourseService courseService, AdditionalMaterialsService additionalMaterialsService) {
        this.studentService = studentService;
        this.studentPracticeService = studentPracticeService;
        this.personService = personService;
        this.studentReportService = studentReportService;
        this.timeSlotService = timeSlotService;
        this.courseService = courseService;
        this.additionalMaterialsService = additionalMaterialsService;
    }


    @GetMapping
    public ResponseEntity<Collection<StudentEntity>> get(
            @RequestParam(required = false) Optional<Long> courseId,
            @RequestParam(required = false) Optional<Long> personId,
            @RequestParam(required = false) boolean inactive,
            @RequestParam(required = false) boolean ban) {
        if (courseId.isEmpty()) {
            if (personId.isEmpty()) {
                return getResponse(studentService.get(inactive, ban));
            }
            Optional<PersonEntity> person = personService.get(personId.get());
            return person.isEmpty() ? badRequest()
                    : getResponse(studentService.getCoursesOfPerson(person.get(), inactive, ban));
        }
        if (personId.isEmpty()) {
            return getResponse(studentService.getStudentsOfCourse(courseId.get(), inactive, ban));
        }
        Optional<PersonEntity> person = personService.get(personId.get());
        Optional<CourseEntity> course = courseService.get(courseId.get());
        return person.isEmpty() || course.isEmpty() ? badRequest()
                : getResponse(studentService.get(person.get(), course.get(), inactive, ban)
                        .map(List::of).orElseGet(List::of));
    }


    @GetMapping("/chapters/{studentId}/{number}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<StudentChapterDto> getChapter(@PathVariable long studentId,
            @PathVariable int number) {
        return studentService.get(studentId)
                .map(student -> getResponse(studentService.getOpenedChapter(student, number)))
                .orElse(badRequest());
    }


    @GetMapping("/chapters/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Collection<ShortChapterDto>> getOpenChapters(
            @PathVariable long studentId) {
        return studentService.get(studentId)
                .map(student -> getResponse(studentService.getChapters(student)))
                .orElse(badRequest());
    }

    
    @PutMapping("/chapters/state/{id}/{newState}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<StudentChapterDto> stateChapter(@PathVariable long studentChapterId,
            @PathVariable String newStateString) {
        Optional<StudentChapterEntity>  chapter = studentService.getStudentChapter(studentChapterId);
        ChapterState newState = ChapterState.valueOf(newStateString);
        return newState != null && chapter.isPresent() ? getResponse(studentService.changeState(newState)) 
                : badRequest();
    }
    
    
    
/*
    @GetMapping("/practices/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Collection<PracticeDto>> getAllMyPractices() {
        return getResponse(studentPracticeService.getAllPracticesByStudent().stream()
                .map(Converter::convertToPractice).collect(Collectors.toSet()));
    }
*/

    @GetMapping("/practices/{practiceState}")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<Collection<MentorPracticeDto>> getPracticeWithStateFilter(
            @PathVariable String practiceState) {
        PracticeState state = PracticeState.fromString(practiceState);
        List<StudentPracticeEntity> students = studentPracticeService.getAllStudentsByState(state);
        return getResponse(students.stream().map(MentorPracticeDto::map).toList());
    }


    @GetMapping("/practices/states")
    public ResponseEntity<Collection<String>> getPracticeStates() {
        List<String> practiceStates = Arrays.stream(PracticeState.values())
                .map(state -> state.name().toLowerCase()).toList();
        return getResponse(practiceStates);
    }


    @PostMapping("/practices")
    public ResponseEntity<StudentPracticeDto> setPracticeState(
            @RequestBody StudentPracticeDto studentPracticeDto) {
        return postResponse(
                StudentPracticeDto.map(studentPracticeService.changeState(studentPracticeDto)));
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


    @PostMapping("/reports/course/{slug}")
    public ResponseEntity<StudentReportDto> postStudentReport(@PathVariable String slug,
            Principal principal, @RequestBody StudentReportCreationDto studentReportCreationDto) {
        List<PersonEntity> persons = personService.get(principal.getName());
        Optional<StudentReportEntity> reportEntity =
                studentReportService.createStudentReport(persons.get(0), studentReportCreationDto);
        return postResponse(Optional
                .ofNullable(reportEntity.isEmpty() ? null : Converter.convert(reportEntity.get())));
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
        return studentService.get(studentId)
                .map(student -> getResponse(studentService.getAdditionalMaterials(student)))
                .orElse(badRequest());
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

}
