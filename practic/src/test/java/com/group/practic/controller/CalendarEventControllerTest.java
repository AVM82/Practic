package com.group.practic.controller;

import com.group.practic.dto.EventDto;
import com.group.practic.dto.MessageSendingResultDto;
import com.group.practic.service.CalendarEventService;
import com.group.practic.service.EmailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@Slf4j
 class CalendarEventControllerTest {

    private CalendarEventController controller;

    @Mock
    private CalendarEventService eventService;

    @Mock
    private EmailSenderService emailSenderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new CalendarEventController(eventService, emailSenderService);
    }

    @Test
    void testSendMailSuccess() {
        EventDto eventDto = new EventDto(
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                "Test Subject",
                "Test Description"
        );

        MessageSendingResultDto expectedResponse = new MessageSendingResultDto("Success message", 1);
        when(eventService.sendEventMessageAllPerson(emailSenderService, eventDto))
                .thenReturn(expectedResponse);
        ResponseEntity<MessageSendingResultDto> responseEntity = controller.sendMail(eventDto);
        verify(eventService, times(1))
                .sendEventMessageAllPerson(emailSenderService, eventDto);
        assert (responseEntity.getStatusCode() == HttpStatus.OK);
        assert (responseEntity.getBody().getMessage().equals(expectedResponse.getMessage()));
        assert (responseEntity.getBody().getSuccessfulDeliveries() == expectedResponse.getSuccessfulDeliveries());

    }

    @Test
    void testSendMailValidationError() {
        EventDto eventDto = new EventDto(
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                "Val",
                "inV"
        );
        when(eventService.sendEventMessageAllPerson(emailSenderService, eventDto))
                .thenReturn(new MessageSendingResultDto("Invalid data", 0));
        ResponseEntity<MessageSendingResultDto> responseEntity = controller.sendMail(eventDto);
        verify(eventService, times(1))
                .sendEventMessageAllPerson(emailSenderService, eventDto);
        assert (responseEntity.getStatusCode() == HttpStatus.OK);
        assert (responseEntity.getBody().getMessage().equals("Invalid data"));
    }
}

