package edu.eci.labinfo.bookinglab.data;


import java.time.LocalDateTime;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.eci.labinfo.bookinglab.model.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByInitialDateTime(LocalDateTime initialDateTime);
    Optional<Reservation> findByInitialDateTimeAndEndDateTime(LocalDateTime initialDateTime, LocalDateTime endDateTime);
    Optional<Reservation> findByProfessor(String professor);
    //Optional<Reservation> findByBLaboratory(String laboratoryName);

}
