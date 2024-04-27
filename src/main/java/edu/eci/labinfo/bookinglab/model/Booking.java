package edu.eci.labinfo.bookinglab.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Representa una reserva de un laboratorio
 *
 * @author Daniel Antonio Santanilla
 * @author David Eduardo Valencia
 * @version 1.0
 */
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
