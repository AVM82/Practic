package com.group.practic.controller;

import com.group.practic.dto.EventDto;
import com.group.practic.dto.MessageSendingResultDto;
import com.group.practic.service.CalendarEventService;
import com.group.practic.service.EmailSenderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
public class CalendarEventController {

    @Autowired
    CalendarEventService eventService;

    @Autowired
    EmailSenderService emailSenderService;

    @PostMapping("/sendEvent")
    public ResponseEntity<MessageSendingResultDto> sendMail(@Valid @RequestBody EventDto eventDto) {
        MessageSendingResultDto messageAllPerson =
                eventService.sendEventMessageAllPerson(emailSenderService, eventDto);
        return ResponseEntity.ok(messageAllPerson);
    }
}
