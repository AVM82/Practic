package com.group.practic.service;

import com.group.practic.entity.TimeSlotEntity;
import com.group.practic.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;

    @Autowired
    public TimeSlotService(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }


    public Map<String, List<TimeSlotEntity>> getAvailableTimeSlots() {

        List<TimeSlotEntity> timeSlotList = timeSlotRepository.findAllByAvailabilityTrue();
        Map<String, List<TimeSlotEntity>> slotMap = new HashMap<>();
        for (TimeSlotEntity timeSlot : timeSlotList) {
            String date = timeSlot.getDate().toString();
            // Check if the date is already a key in the map
            if (!slotMap.containsKey(date)) {
                slotMap.put(date, new ArrayList<>());
            }
            if(timeSlot.isAvailability()) {
                //Add the TimeSlot to the list associated with the date
                slotMap.get(date).add(timeSlot);
            }
        }
        return slotMap;
    }

    public void fillTimeSlots() {
        List<String> timeList = new ArrayList<>(Arrays.asList("17:00",
                "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00"));
        LocalDate currentDate = LocalDate.now();
        LocalDate endDate = currentDate.plusDays(4); // Заполняем на 14 дней

        while (currentDate.isBefore(endDate)) {
            // Создаем Timeslot на текущую дату
            Optional<List<TimeSlotEntity>> timeSlotEntities = timeSlotRepository.findAllByDate(currentDate);

            if (timeSlotEntities.get().isEmpty()) {
                for (String time : timeList) {
                    TimeSlotEntity timeSlot = new TimeSlotEntity();
                    timeSlot.setDate(currentDate);

                    timeSlot.setTime(LocalTime.parse(time));
                    timeSlot.setAvailability(true); // Предполагаем, что слот доступен

                    // Сохраняем Timeslot в базу данных
                    timeSlotRepository.save(timeSlot);
                }
            }
            // Переходим к следующей дате
            currentDate = currentDate.plusDays(1);
        }
    }
}
