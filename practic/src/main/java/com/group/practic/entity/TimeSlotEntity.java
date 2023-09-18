package com.group.practic.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "report_timeslots")
public class TimeSlotEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;
    LocalDate date;
    LocalTime time;
    boolean availability = true;


    public TimeSlotEntity( LocalDate date, LocalTime time) {
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
