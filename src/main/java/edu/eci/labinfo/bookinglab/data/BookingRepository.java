package edu.eci.labinfo.bookinglab.data;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.eci.labinfo.bookinglab.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByInitialTimeSlot(LocalDateTime initialTimeSlot);
    Optional<Booking> findByInitialTimeSlotAndFinalTimeSlot(LocalDateTime initialTimeSlot, LocalDateTime finalTimeSlot);
    Optional<Booking> findByTeacher(String teacher);
    Optional<Booking> findByLaboratoryLaboratoryName(String laboratoryName);

}
