package edu.eci.labinfo.bookinglab.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.GregorianCalendar;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Booking {

    @Id
    private Long booking_id;
    private Calendar Calendar = new GregorianCalendar();
    
    @OneToMany(mappedBy = "idReservation")
    private List<Reservation> reservations;

}
