package edu.eci.labinfo.bookinglab.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.eci.labinfo.bookinglab.model.Booking;
import edu.eci.labinfo.bookinglab.model.Reservation;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
}
