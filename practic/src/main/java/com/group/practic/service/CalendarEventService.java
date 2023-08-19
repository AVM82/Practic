package com.group.practic.service;

import com.group.practic.dto.EventDto;
import com.group.practic.dto.SendMessageDto;
import com.group.practic.entity.PersonEntity;
import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalendarEventService {

    PersonService personService;

    @Autowired
    CalendarEventService(PersonService personService) {
        this.personService = personService;
    }

    public String sendEventMessageAllPerson(Sender sender, EventDto eventDto) {
        String eventMessage = getEventMessage(eventDto);
        List<PersonEntity> allPerson = personService.get();

        for (PersonEntity person : allPerson) {
            sender.sendMessage(new SendMessageDto(person.getContacts(), eventMessage,
                    "Запрошуємо послухати доповідь"));
        }
        return eventMessage;
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

        return String.format("Привіт, на менторському курсі по Java %s о %s \n"
                        + "Відбудеться доповідь на тему: %s. Запрошуємо тебе долучитися до нас у \n"
                        +
                        "Discord кімнату m-java -> https://discord.com/channels/534496884849639455/843878435067002930\n"
                        + "link на додання події у google Calendar -> %s", startDate, startTime,
                eventDto.getSubjectReport(), linkCalendar);
    }

    private String getLink(EventDto eventDto) {
        String header = ("Доповідь на тему " + eventDto.getSubjectReport()).replace(" ", "+");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
        String startDate = eventDto.getStartEvent().format(formatter);
        String endDate = eventDto.getEndEvent().format(formatter);
        String description = eventDto.getDescription().replace(" ", "+");
        return String.format("https://calendar.google.com/calendar/r/eventedit?text=%s&dates=%s/%s&"
                + "details=%s&location=discord+m-java", header, startDate, endDate, description);
    }
}
