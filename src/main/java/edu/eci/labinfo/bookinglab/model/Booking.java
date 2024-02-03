package edu.eci.labinfo.bookinglab.model;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
    @Column(length = 500)
    private String observation;;

    @OneToOne
    @JoinColumn(name = "duration_id", referencedColumnName = "durationId")
    private Duration duration;

}
