package edu.eci.labinfo.bookinglab.data;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.eci.labinfo.bookinglab.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByTeacher(String teacher);

    List<Booking> findByLaboratory(String laboratory);

    List<Booking> findByCourse(String course);

    List<Booking> findByDay(DayOfWeek day);

}
