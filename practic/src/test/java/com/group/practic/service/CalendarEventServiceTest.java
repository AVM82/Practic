package com.group.practic.service;

import com.group.practic.dto.EventDto;
import com.group.practic.dto.MessageSendingResultDto;
import com.group.practic.dto.SendMessageDto;
import com.group.practic.entity.PersonEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

 class CalendarEventServiceTest {

    @InjectMocks
    private CalendarEventService calendarEventService;

    @Mock
    private PersonService personService;

    @Mock
    private Sender sender;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(calendarEventService, "emailMessage", "|+\n" +
                "      Привіт, на менторському курсі по Java %s о %s\n" +
                "      Відбудеться доповідь на тему \"%s\". Запрошуємо тебе долучитися до нас у\n" +
                "      Discord кімнату m-java -> https://discord.com/channels/534496884849639455/843878435067002930\n" +
                "      link на додання події у google Calendar -> %s");
        ReflectionTestUtils.setField(calendarEventService, "emailMessageHeader", "|\n" +
                "      Заходь послухати доповідь");
        ReflectionTestUtils.setField(calendarEventService, "calendarHeader", "|+\n" +
                "      Доповідь на тему - %s");
        ReflectionTestUtils.setField(calendarEventService, "calendarLocation", "discord m-java");
    }

    @Test
    void testSendEventMessageAllPerson() {
        EventDto eventDto = new EventDto();
        eventDto.setStartEvent(LocalDateTime.now());
        eventDto.setEndEvent(LocalDateTime.now());
        eventDto.setSubjectReport("Test Event");
        eventDto.setDescription("Test Description");
        List<PersonEntity> personEntities = new ArrayList<>();
        PersonEntity person1 = new PersonEntity();
        person1.setEmail("person1@example.com");
        personEntities.add(person1);
        Mockito.when(personService.get()).thenReturn(personEntities);
        Mockito.when(sender.sendMessage(Mockito.any(SendMessageDto.class))).thenReturn(true);

        MessageSendingResultDto result = calendarEventService.sendEventMessageAllPerson(sender, eventDto);

        assertEquals(1, result.getSuccessfulDeliveries());
    }
}
