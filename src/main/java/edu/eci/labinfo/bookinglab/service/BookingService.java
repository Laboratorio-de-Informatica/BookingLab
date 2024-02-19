package edu.eci.labinfo.bookinglab.service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.eci.labinfo.bookinglab.data.BookingRepository;
import edu.eci.labinfo.bookinglab.model.Booking;
import edu.eci.labinfo.bookinglab.model.BookingLabException;
import edu.eci.labinfo.bookinglab.model.Laboratory;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public Booking createReservation(Booking booking) throws BookingLabException {
        if (booking.getInitialTimeSlot().isAfter(booking.getFinalTimeSlot())) {
            throw new BookingLabException(BookingLabException.INITDATE_MAYOR_ENDDATE);
        }
        return bookingRepository.save(nameFormatter(booking));
    }

    private Booking nameFormatter(Booking booking) {
        String name = booking.getTeacher();
        booking.setTeacher(name.toUpperCase());
        return booking;
    }

    public List<Booking> getAllReservations() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getReservationById(Long id) throws BookingLabException {
        if (bookingRepository.findById(id).isEmpty()) {
            throw new BookingLabException(BookingLabException.RESERVATION_NOT_FOUND);
        }
        return bookingRepository.findById(id);
    }

    public List<Booking> getReservationByTeacher(String teacher) {
        return bookingRepository.findByTeacher(teacher.toUpperCase());
    }

    public List<Booking> getReservationByLaboratory(String laboratory) {
        return bookingRepository.findByLaboratory(laboratory);
    }

    public List<Booking> getReservationByCourse(String course) {
        return bookingRepository.findByCourse(course);
    }

    public Booking updateReservation(Booking booking) throws BookingLabException {
        if (bookingRepository.findById(booking.getBookingId()).isEmpty()) {
            throw new BookingLabException(BookingLabException.RESERVATION_NOT_FOUND);
        }
        booking = nameFormatter(booking);
        return bookingRepository.save(booking);
    }

    public void deleteReservation(Booking booking) {
        bookingRepository.deleteById(booking.getBookingId());
    }

    public DayOfWeek[] getWeekDays() {
        return DayOfWeek.values();
    }

    public List<String> getLaboratories() {
        return Laboratory.getAllLaboratories();
    }

}
