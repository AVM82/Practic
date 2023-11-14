package com.group.practic.controller;

import static com.group.practic.util.ResponseUtils.getResponse;
import static com.group.practic.util.ResponseUtils.postResponse;

import com.group.practic.entity.TimeSlotEntity;
import com.group.practic.service.TimeSlotService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
