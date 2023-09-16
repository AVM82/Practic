package com.group.practic.repository;

import com.group.practic.entity.TimeSlotEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeSlotRepository extends JpaRepository< TimeSlotEntity, Long> {
    List<TimeSlotEntity> findAllByAvailabilityTrueOrderByTime();
    Optional<List<TimeSlotEntity>> findAllByDateOrderByDate(LocalDate date);

}
