package edu.eci.labinfo.bookinglab.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Laboratory {

    @Id
    private String laboratoryName;
    private int availableComputers;

    @OneToMany(mappedBy = "laboratory")
    @ToString.Exclude
    private List<Booking> bookings;

    public Laboratory(String laboratoryName, int availableComputers) {
        this.laboratoryName = laboratoryName;
        this.availableComputers = availableComputers;
    }

}
