package com.group.practic.service;

import com.group.practic.PropertyLoader;
import com.group.practic.entity.TimeSlotEntity;
import com.group.practic.repository.TimeSlotRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




@Service
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;

    @Autowired
    public TimeSlotService(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }


    public Map<String, List<TimeSlotEntity>> getAvailableTimeSlots() {

        List<TimeSlotEntity> timeSlotList = timeSlotRepository
                .findAllByAvailabilityTrueOrderByTime();
        Map<String, List<TimeSlotEntity>> slotMap = new HashMap<>();
        for (TimeSlotEntity timeSlot : timeSlotList) {
            String date = timeSlot.getDate().toString();
            slotMap.computeIfAbsent(date, k -> new ArrayList<>());
            if (timeSlot.isAvailability()) {
                slotMap.get(date).add(timeSlot);
            }
        }
        return slotMap;
    }

    public Optional<TimeSlotEntity> updateTimeSlotAvailability(Long timeslotId) {
        Optional<TimeSlotEntity> timeslotOp = timeSlotRepository.findById(timeslotId);

        if (timeslotOp.isPresent()) {
            TimeSlotEntity timeSlot = timeslotOp.get();
            timeSlot.setAvailability(false);
            return Optional.of(timeSlotRepository.save(timeSlot));
        }
        return Optional.empty();
    }


    public Optional<List<TimeSlotEntity>> fillTimeSlots() {
        PropertyLoader loader = new PropertyLoader("practic/reportTimeslot.properties");
        if (loader.initialized) {
            int reportDuration = Integer.parseInt(
                    loader.getProperty("reportDurationMinutes", "30"));
            int daysNum = Integer.parseInt(loader.getProperty("numberOfDays", "5"));
            LocalDate currentDate = LocalDate.now();
            LocalDate endDate = currentDate.plusDays(daysNum);

            while (currentDate.isBefore(endDate)) {
                Optional<List<TimeSlotEntity>> timeSlotEntities = timeSlotRepository
                        .findAllByDateOrderByDate(currentDate);
                LocalTime firstReportTime = LocalTime.parse(loader.getProperty("firstReportTime",
                        "16:00"));
                LocalTime endTime = LocalTime.parse(loader.getProperty("lastReportTime", "22:00"));
                if (timeSlotEntities.isPresent() && timeSlotEntities.get().isEmpty()) {
                    while (firstReportTime.isBefore(endTime.plusMinutes(1))) {
                        createTimeslot(currentDate, firstReportTime);
                        firstReportTime = firstReportTime.plusMinutes(reportDuration);
                    }
                }
                currentDate = currentDate.plusDays(1);
            }
        }
        return timeSlotRepository.findAllByDateOrderByDate(LocalDate.now());
    }

    public Optional<TimeSlotEntity> createTimeslot(LocalDate date, LocalTime time) {
        return Optional.of(timeSlotRepository
                .save(new TimeSlotEntity(date, time)));
    }
}
