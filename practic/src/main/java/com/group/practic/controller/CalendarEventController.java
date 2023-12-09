package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.postResponse;

import com.group.practic.dto.EventDto;
import com.group.practic.dto.MessageSendingResultDto;
import com.group.practic.service.CalendarEventService;
import com.group.practic.service.CourseService;
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

    CourseService courseService;


    @Autowired
    public CalendarEventController(CalendarEventService eventService, CourseService courseService) {
        this.eventService = eventService;
        this.courseService = courseService;
    }


    @PostMapping("/sendEvent/{slug}")
    public ResponseEntity<MessageSendingResultDto> sendMail(@RequestBody EventDto eventDto,
            @PathVariable String slug) {
        return postResponse(courseService.get(slug)
                .map(course -> eventService.sendEventMessageAllPerson(eventDto, course)));
    }

}
