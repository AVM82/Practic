package com.group.practic.service;

import com.group.practic.dto.StudentReportCreationDto;
import com.group.practic.entity.ChapterEntity;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.StudentChapterEntity;
import com.group.practic.entity.StudentReportEntity;
import com.group.practic.entity.TimeSlotEntity;
import com.group.practic.enumeration.ReportState;
import com.group.practic.repository.StudentReportRepository;
import com.group.practic.repository.TimeSlotRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StudentReportService {

    private final StudentReportRepository studentReportRepository;

    private final CourseService courseService;

    private final TimeSlotRepository timeSlotRepository;

    private final TimeSlotService timeSlotService;

    static final List<ReportState> ACTUAL_STATES =
            List.of(ReportState.STARTED, ReportState.ANNOUNCED);


    @Autowired
    public StudentReportService(StudentReportRepository studentReportRepository,
            CourseService courseService, TimeSlotRepository timeSlotRepository,
            TimeSlotService timeSlotService) {
        this.studentReportRepository = studentReportRepository;
        this.courseService = courseService;
        this.timeSlotRepository = timeSlotRepository;
        this.timeSlotService = timeSlotService;
    }


    public long getActualReportCount(ChapterEntity chapter) {
        return studentReportRepository.countByStudentChapterChapterAndStateIn(chapter,
                ACTUAL_STATES);
    }


    public List<List<StudentReportEntity>> getAllStudentsActualReports(String slug) {
        setStatesOfFinishedReports();
        Optional<CourseEntity> course = courseService.get(slug);
        List<List<StudentReportEntity>> result = Collections.emptyList();
        if (course.isPresent()) {
            result = new ArrayList<>();
            for (ChapterEntity chapter : course.get().getChapters()) {
                result.add(studentReportRepository
                        .findAllByStudentChapterChapterAndStateInOrderByTimeSlotId(chapter,
                                ACTUAL_STATES));
            }
        }
        return result;
    }


    public Optional<StudentReportEntity> createStudentReport(StudentChapterEntity studentChapter,
            StudentReportCreationDto studentReportCreationDto) {
        return timeSlotRepository.findById(studentReportCreationDto.timeslotId()).map(timeSlot -> {
            timeSlotService.updateTimeSlotAvailability(timeSlot.getId(), false);
            return studentReportRepository.save(new StudentReportEntity(studentChapter, timeSlot,
                    studentReportCreationDto.title()));
        });
    }


    public Optional<StudentReportEntity> changeReportLikeList(long reportId, long studentId) {
        Optional<StudentReportEntity> report = studentReportRepository.findById(reportId);
        if (report.isPresent()) {
            StudentReportEntity reportEntity = report.get();
            List<Long> reportLikedPersonsIdList = reportEntity.getLikedPersonsIdList();
            if (reportLikedPersonsIdList.contains(studentId)) {
                reportLikedPersonsIdList.remove(studentId);
            } else {
                reportLikedPersonsIdList.add(studentId);
            }
            reportEntity.setLikedPersonsIdList(reportLikedPersonsIdList);
            return Optional.of(studentReportRepository.save(reportEntity));
        }
        return Optional.empty();
    }


    public void setStatesOfFinishedReports() {
        List<StudentReportEntity> result = studentReportRepository.findAll();
        for (StudentReportEntity report : result) {
            if (report.getTimeSlot().getDate().isBefore(LocalDate.now())) {
                report.setState(ReportState.FINISHED);
                studentReportRepository.save(report);
            }
        }
    }


    public Optional<StudentReportEntity> deleteReport(long reportId) {
        Optional<StudentReportEntity> report = studentReportRepository.findById(reportId);
        if (report.isPresent()) {
            StudentReportEntity reportEntity = report.get();
            Optional<TimeSlotEntity> timeslot =
                    timeSlotRepository.findById(report.get().getTimeSlot().getId());
            if (timeslot.isPresent()) {
                timeslot.ifPresent(timeSlot -> timeSlotService
                        .updateTimeSlotAvailability(timeSlot.getId(), true));
            }
            studentReportRepository.delete(reportEntity);
            return Optional.of(reportEntity);
        }
        return Optional.empty();
    }


    public Optional<StudentReportEntity> changeReport(StudentReportCreationDto reportDto) {
        Optional<StudentReportEntity> report = studentReportRepository.findById(reportDto.id());
        if (report.isPresent()) {
            StudentReportEntity reportEntity = report.get();
            reportEntity.setTitle(reportDto.title());
            if (reportDto.timeslotId() != reportEntity.getTimeSlot().getId()) {
                Optional<TimeSlotEntity> newTimeslot =
                        timeSlotRepository.findById(reportDto.timeslotId());
                if (newTimeslot.isPresent()) {
                    timeSlotService.updateTimeSlotAvailability(newTimeslot.get().getId(), false);
                    TimeSlotEntity oldTimeslot = reportEntity.getTimeSlot();
                    timeSlotService.updateTimeSlotAvailability(oldTimeslot.getId(), true);
                    reportEntity.setTimeSlot(newTimeslot.get());
                }
            }
            return Optional.of(studentReportRepository.save(reportEntity));
        }
        return Optional.empty();
    }

}
