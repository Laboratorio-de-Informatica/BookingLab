package edu.eci.labinfo.bookinglab.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Booking {
    
    @Id
    private long idBooking;
    
    private LocalDate iniDate;
    private LocalDate endDate;
}
