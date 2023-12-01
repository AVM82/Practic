package com.group.practic.service;

import com.group.practic.dto.EventDto;
import com.group.practic.dto.MessageSendingResultDto;
import com.group.practic.dto.SendMessageDto;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.StudentEntity;
import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CalendarEventService {

    StudentService studentService;

    @Value("${email.message.body}")
    private String emailMessage;

    @Value("${email.message.header}")
    private String emailMessageHeader;

    @Value("${email.calendar.header}")
    private String calendarHeader;

    @Value("${email.calendar.location}")
    private String calendarLocation;

    @Autowired
    CalendarEventService(StudentService studentService) {
        this.studentService = studentService;
    }

    public MessageSendingResultDto sendEventMessageAllPerson(Sender sender, EventDto eventDto,
            CourseEntity course) {
        String eventMessage = getEventMessage(eventDto);
        List<StudentEntity> allStudentsOfCourse = studentService
                .getStudentsOfCourse(course, false, false);
        int counter = 0;
        for (StudentEntity student : allStudentsOfCourse) {
            if (sender.sendMessage(new SendMessageDto(student.getPerson().getEmail(), eventMessage,
                    emailMessageHeader))) {
                counter++;
            }
        }
        return new MessageSendingResultDto(eventMessage, counter);
    }

    private String getEventMessage(EventDto eventDto) {
        LocalDateTime startLocalDate = eventDto.getStartEvent();
        Locale ukLocale = new Locale("uk", "UA");
        String month =
                new DateFormatSymbols(ukLocale).getMonths()[startLocalDate.getMonthValue() - 1]
                        .toLowerCase(ukLocale);
        String linkCalendar = getLink(eventDto);
        String startDate = startLocalDate.getDayOfMonth() + " " + month;
        String startTime =
                String.format("%02d:%02d", startLocalDate.getHour(), startLocalDate.getMinute());
        return String.format(emailMessage, startDate, startTime,
                eventDto.getSubjectReport(), linkCalendar);
    }

    private String getLink(EventDto eventDto) {
        String headerMessage = String.format(calendarHeader, eventDto.getSubjectReport());
        String header = headerMessage.replace(" ", "+");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
        String startDate = eventDto.getStartEvent().format(formatter);
        String endDate = eventDto.getEndEvent().format(formatter);
        String description = eventDto.getDescription().replace(" ", "+");
        String location = calendarLocation.replace(" ", "+");
        return String.format("https://calendar.google.com/calendar/r/eventedit?text=%s&dates=%s/%s&"
                + "details=%s&location=%s", header, startDate, endDate, description, location);
    }
}
