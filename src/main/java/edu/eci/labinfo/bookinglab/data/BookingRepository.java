package edu.eci.labinfo.bookinglab.data;

import java.time.LocalTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.eci.labinfo.bookinglab.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByInitialTimeSlot(LocalTime initialTimeSlot);

    Optional<Booking> findByInitialTimeSlotAndFinalTimeSlot(LocalTime initialTimeSlot, LocalTime finalTimeSlot);

    Optional<Booking> findByTeacher(String teacher);

    Optional<Booking> findByLaboratory(String laboratory);

}
