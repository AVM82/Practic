package com.group.practic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.group.practic.entity.TimeSlotEntity;
import com.group.practic.repository.TimeSlotRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@Slf4j
class TimeSlotServiceTest {
    /*
    @InjectMocks
    private TimeSlotService timeSlotService;
    @Mock
    private TimeSlotRepository timeSlotRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetAvailableTimeSlots() {
        List<TimeSlotEntity> timeSlotList = new ArrayList<>();
        timeSlotList.add(new TimeSlotEntity(LocalDate.of(2023, 10, 21), LocalTime.of(9, 0)));
        timeSlotList.add(new TimeSlotEntity(LocalDate.of(2023, 10, 22), LocalTime.of(10, 0)));
        timeSlotList.add(new TimeSlotEntity(LocalDate.of(2021, 10, 22), LocalTime.of(9, 0)));
        timeSlotList.add(new TimeSlotEntity(LocalDate.of(2023, 10, 22), LocalTime.of(10, 0)));

        when(timeSlotRepository.findAllByAvailabilityTrueOrderByTime()).thenReturn(timeSlotList);

        Map<String, List<TimeSlotEntity>> result = timeSlotService.getAvailableTimeSlots();

        verify(timeSlotRepository, times(1)).findAllByAvailabilityTrueOrderByTime();

        assertEquals(3, result.size());
        assertEquals(1, result.get("2023-10-21").size());
        assertEquals(2, result.get("2023-10-22").size());
        assertEquals(1, result.get("2021-10-22").size());
        for (List<TimeSlotEntity> slots : result.values()) {
            for (TimeSlotEntity slot : slots) {
                assertTrue(slot.isAvailability());
            }
        }

        List<TimeSlotEntity> sortedSlots = timeSlotList.stream()
                .filter(TimeSlotEntity::isAvailability)
                .sorted(Comparator.comparing(TimeSlotEntity::getDate))
                .collect(Collectors.toList());
        List<TimeSlotEntity> resultSlots = result.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
        assertEquals(sortedSlots, resultSlots);
    }

    @Test
    void testUpdateTimeSlotAvailability() {
        Long timeslotId = 1L;
        TimeSlotEntity timeSlotEntity = new TimeSlotEntity();
        timeSlotEntity.setId(timeslotId);
        timeSlotEntity.setAvailability(true);

        when(timeSlotRepository.findById(timeslotId)).thenReturn(Optional.of(timeSlotEntity));
        when(timeSlotRepository.save(any(TimeSlotEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Optional<TimeSlotEntity> result = timeSlotService.updateTimeSlotAvailability(timeslotId);
        verify(timeSlotRepository, times(1)).findById(timeslotId);
        verify(timeSlotRepository, times(1)).save(timeSlotEntity);

        assertTrue(result.isPresent());
        assertFalse(result.get().isAvailability());
        assertEquals(timeslotId, result.get().getId());
    }

    @Test
    void testUpdateTimeSlotAvailabilityWhenTimeSlotNotFound() {
        Long timeslotId = 1L;

        when(timeSlotRepository.findById(timeslotId)).thenReturn(Optional.empty());

        Optional<TimeSlotEntity> result = timeSlotService.updateTimeSlotAvailability(timeslotId);

        verify(timeSlotRepository, times(1)).findById(timeslotId);

        assertFalse(result.isPresent());
    }

    @Test
    void testCreateTimeslot() {
        LocalDate date = LocalDate.of(2023, 10, 15);
        LocalTime time = LocalTime.of(14, 30);

        TimeSlotEntity expectedTimeSlot = new TimeSlotEntity(date, time);

        when(timeSlotRepository.save(any(TimeSlotEntity.class))).thenReturn(expectedTimeSlot);

        Optional<TimeSlotEntity> createdTimeSlot = timeSlotService.createTimeslot(date, time);

        assertEquals(expectedTimeSlot, createdTimeSlot.orElse(null));
    }
    */
}
