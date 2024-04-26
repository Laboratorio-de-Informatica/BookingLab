package edu.eci.labinfo.bookinglab.data;

import edu.eci.labinfo.bookinglab.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

/**
 * Interfaz que define las operaciones de la base de datos para las reservas
 *
 * @author Andres Camilo Oniate
 * @author Daniel Antonio Santanilla
 * @version 1.0
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByTeacher(String teacher);
    List<Booking> findByLaboratory(String laboratory);
    List<Booking> findByCourse(String course);
    List<Booking> findByDay(DayOfWeek day);

}
