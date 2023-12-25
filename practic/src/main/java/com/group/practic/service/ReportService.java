package com.group.practic.service;

import com.group.practic.dto.ReportDto;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.ReportEntity;
import com.group.practic.entity.StudentChapterEntity;
import com.group.practic.entity.TopicReportEntity;
import com.group.practic.enumeration.ReportState;
import com.group.practic.repository.ReportRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ReportService {

    public static final int AHEAD_DAYS = 7;

    public static final int MAX_REPORS_PER_DAY = 3;

    public static final int LIKES_TO_APPROOVE = 3;

    public static final List<ReportState> ACTUAL_STATES =
            List.of(ReportState.ANNOUNCED, ReportState.STARTED, ReportState.FINISHED);


    ReportRepository reportRepository;

    PersonService personService;

    LocalDate sweepDay = LocalDate.of(1970, 1, 1);

    @Autowired
    public ReportService(ReportRepository reportRepository, PersonService personService) {
        this.reportRepository = reportRepository;
        this.personService = personService;
        this.cancelMissedReports(LocalDate.now());
    }


    public Optional<ReportEntity> get(long id) {
        return reportRepository.findById(id);
    }


    public List<ReportEntity> getChapterActual(CourseEntity course, LocalDate date, int number) {
        return reportRepository
                .findAllByCourseAndDateGreaterThanEqualAndStateNotAndChapterNumberIsOrderByDateAsc(
                        course, date, ReportState.CANCELLED, number);
    }


    public List<ReportEntity> getActual(CourseEntity course, LocalDate date) {
        cancelMissedReports(date);
        return reportRepository
                .findAllByCourseAndDateGreaterThanEqualAndStateNotOrderByChapterNumberAscDateAsc(
                        course, date, ReportState.CANCELLED);
    }


    public List<Boolean> getFreeDaysFrom(CourseEntity course, LocalDate date) {
        List<Boolean> result = new ArrayList<>();
        for (int i = 0; i < AHEAD_DAYS; i++) {
            result.add(isRightDay(course, date.plusDays(i)));
        }
        return result;
    }


    public boolean isRightDay(CourseEntity course, LocalDate date) {
        return reportRepository.countByCourseAndDateEqualsAndStateNot(course, date,
                ReportState.CANCELLED) < MAX_REPORS_PER_DAY;
    }


    protected boolean cancelMissedReports(LocalDate date) {
        if (date.isAfter(sweepDay)) {
            sweepDay = date;
            return 0 < reportRepository.cancelMissedAnnouncedReport(sweepDay);
        }
        return false;
    }


    public boolean approveReportByCertainLikes(ReportEntity report) {
        boolean result = report.getLikedPersonIds().size() >= LIKES_TO_APPROOVE;
        if (result) {
            report.setState(ReportState.APPROVED);
        }
        return result;
    }


    public ReportEntity create(Optional<StudentChapterEntity> studentChapter, CourseEntity course,
            PersonEntity person, LocalDate date, TopicReportEntity topic) {
        return studentChapter.isPresent() && isRightDay(course, date)
                ? reportRepository
                        .save(new ReportEntity(studentChapter.get(), course, person, date, topic))
                : new ReportEntity();
    }


    public ReportEntity create(CourseEntity course, PersonEntity person, LocalDate date,
            int chapterNumber, TopicReportEntity topic) {
        return isRightDay(course, date)
                ? reportRepository
                        .save(new ReportEntity(course, person, date, chapterNumber, topic))
                : new ReportEntity();
    }


    public ReportEntity update(ReportEntity report, int chapterNumber,
            StudentChapterEntity newStudentChapter, LocalDate newDate, TopicReportEntity newTopic) {
        if (report.getChapterNumber() != chapterNumber) {
            report.setChapterNumber(chapterNumber);
            report.setStudentChapter(newStudentChapter);
        }
        if (!report.getDate().isEqual(newDate) && !isRightDay(report.getCourse(), newDate)) {
            return new ReportEntity();
        }
        report.setDate(newDate);
        report.setTopic(newTopic);
        return reportRepository.save(report);
    }


    public ReportDto changeState(ReportEntity report, ReportState newState) {
        if (report.getState().changeAllowed(newState)) {
            report.setState(newState);
            return ReportDto.map(reportRepository.save(report));
        }
        return null;
    }


    public ReportDto like(ReportEntity report) {
        long meId = PersonService.me().getId();
        long personId = report.getPerson().getId();
        if (meId != personId) {
            if (!report.getLikedPersonIds().add(meId)) {
                report.getLikedPersonIds().remove(meId);
            }
            if (report.getState() != ReportState.APPROVED) {
                approveReportByCertainLikes(report);
            }
            return ReportDto.map(reportRepository.save(report));
        }
        return null;
    }

}
