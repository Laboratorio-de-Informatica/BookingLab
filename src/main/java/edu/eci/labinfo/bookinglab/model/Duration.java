package edu.eci.labinfo.bookinglab.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Duration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long durationId;
    private List<String> selectedDays;
    private int repetitions;
    private int duration;
    
    @OneToOne(mappedBy = "duration")
    private Booking booking;

    public Duration(int repetitions, int duration) {
        this.repetitions = repetitions;
        this.duration = duration;
        this.selectedDays = new ArrayList<>();
    }

}
