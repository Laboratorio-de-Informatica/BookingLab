package edu.eci.labinfo.bookinglab.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    private String teacher;
    private String course;
    private String laboratory;
    private LocalTime initialTimeSlot;
    private LocalTime finalTimeSlot;
    private DayOfWeek day;
    private LocalDate date;
    private boolean canceled;
    @Column(length = 500)
    private String observation;

}
