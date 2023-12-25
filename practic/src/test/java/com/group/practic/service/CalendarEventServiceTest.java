package com.group.practic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.group.practic.dto.EventDto;
import com.group.practic.dto.MessageSendingResultDto;
import com.group.practic.dto.SendMessageDto;
import com.group.practic.entity.CourseEntity;
import com.group.practic.entity.PersonEntity;
import com.group.practic.entity.StudentEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

class CalendarEventServiceTest {

    @InjectMocks
    private CalendarEventService calendarEventService;

    @Mock
    private StudentService studentService;

    @Mock
    private EmailSenderService emailSenderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(calendarEventService, "emailMessage", "|+\n"
                + "      Привіт, на менторському курсі по Java %s о %s\n"
                + "      Відбудеться доповідь на тему \"%s\". "
                + "Запрошуємо тебе долучитися до нас у\n"
                + "      Discord кімнату m-java -> "
                + "https://discord.com/channels/534496884849639455/843878435067002930\n"
                + "      link на додання події у google Calendar -> %s");
        ReflectionTestUtils.setField(calendarEventService, "emailMessageHeader", "|\n"
                + "      Заходь послухати доповідь");
        ReflectionTestUtils.setField(calendarEventService, "calendarHeader", "|+\n"
                + "      Доповідь на тему - %s");
        ReflectionTestUtils.setField(calendarEventService, "calendarLocation", "discord m-java");
    }

    @Test
    void testSendEventMessageAllPerson() {
        EventDto eventDto = new EventDto();
        eventDto.setStartEvent(LocalDateTime.now());
        eventDto.setEndEvent(LocalDateTime.now());
        eventDto.setSubjectReport("Test Event");
        eventDto.setDescription("Test Description");

        List<StudentEntity> studentEntities = new ArrayList<>();
        PersonEntity person1 = new PersonEntity();
        person1.setEmail("person1@example.com");
        CourseEntity course = new CourseEntity("java-dev-tools", "java mentoring course", "svg");
        StudentEntity student1 = new StudentEntity(person1, course);
        studentEntities.add(student1);
        when(studentService.getStudentsOfCourse(course, false, false))
                .thenReturn(studentEntities);
        when(emailSenderService.sendMessage(any(SendMessageDto.class))).thenReturn(true);
        MessageSendingResultDto result =
                calendarEventService.sendEventMessageAllPerson(eventDto, course);

        assertEquals(1, result.getSuccessfulDeliveries());
    }
}
