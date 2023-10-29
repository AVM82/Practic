package com.group.practic.controller;

import com.group.practic.entity.TimeSlotEntity;
import com.group.practic.service.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.group.practic.util.ResponseUtils.getResponse;
import static com.group.practic.util.ResponseUtils.postResponse;

@RestController
@RequestMapping("/api/reports/course")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    @Autowired
    public TimeSlotController(TimeSlotService timeSlotService) {
        this.timeSlotService = timeSlotService;
    }


    @GetMapping("/{slug}/timeslots")
    public ResponseEntity<Map<String, List<TimeSlotEntity>>> getAvailableTimeSlots(
            @PathVariable String slug) {
        return getResponse(Optional.ofNullable(timeSlotService.getAvailableTimeSlots()));
    }


    @PostMapping("/{slug}/timeslots")
    public ResponseEntity<Optional<List<TimeSlotEntity>>> createTimeslots(
            @PathVariable String slug) {
        return postResponse(Optional.ofNullable(timeSlotService.fillTimeSlots()));
    }
}
