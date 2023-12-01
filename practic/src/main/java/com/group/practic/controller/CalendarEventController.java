package com.group.practic.controller;

import com.group.practic.dto.EventDto;
import com.group.practic.dto.MessageSendingResultDto;
import com.group.practic.entity.CourseEntity;
import com.group.practic.service.CalendarEventService;
import com.group.practic.service.CourseService;
import com.group.practic.service.EmailSenderService;
import com.group.practic.util.ResponseUtils;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;






@RestController
@RequestMapping("/api/events")
public class CalendarEventController {

    CalendarEventService eventService;

    EmailSenderService emailSenderService;

    CourseService courseService;


    @Autowired
    public CalendarEventController(CalendarEventService eventService,
            EmailSenderService emailSenderService, CourseService courseService) {
        this.eventService = eventService;
        this.emailSenderService = emailSenderService;
        this.courseService = courseService;
    }


    @PostMapping("/sendEvent/{slug}")
    public ResponseEntity<MessageSendingResultDto> sendMail(@RequestBody EventDto eventDto,
                @PathVariable  String slug) {
        Optional<CourseEntity> course = courseService.get(slug);
        MessageSendingResultDto messageAllPerson = eventService
                .sendEventMessageAllPerson(emailSenderService, eventDto, course.get());
        return course.isEmpty() ? ResponseUtils.badRequest() : ResponseEntity.ok(messageAllPerson);
    }

}
