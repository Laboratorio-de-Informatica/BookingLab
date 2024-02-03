package edu.eci.labinfo.bookinglab.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservation;
    private String professor;
    private String course;
    
    @ManyToOne
    @JoinColumn(name = "laboratoryName")
    private Laboratory bLaboratory;

    private LocalDateTime initialDateTime;
    private LocalDateTime endDateTime;
}
