package com.group.practic.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "report_timeslots")
public class TimeSlotEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;
    @NotNull
    @FutureOrPresent
    LocalDate date;
    @NotNull
    LocalTime time;
    boolean availability = true;


    public TimeSlotEntity(LocalDate date, LocalTime time) {
        this.date = date;
        this.time = time;
    }


    public TimeSlotEntity() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    @Override
    public String toString() {
        return "TimeSlot{"
                +
            "id=" + id
                +
            ", date=" + date
                +
            ", time=" + time
                +
            ", availability=" + availability
                +
            '}';
    }

}
