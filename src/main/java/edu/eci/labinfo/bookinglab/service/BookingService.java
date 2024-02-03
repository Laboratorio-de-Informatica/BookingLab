package edu.eci.labinfo.bookinglab.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.eci.labinfo.bookinglab.data.BookingRepository;
import edu.eci.labinfo.bookinglab.model.Booking;
import edu.eci.labinfo.bookinglab.model.BookingLabException;
import edu.eci.labinfo.bookinglab.model.Day;
import edu.eci.labinfo.bookinglab.model.Laboratory;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public Booking createReservation(Booking booking) throws BookingLabException {
        if (booking.getInitialTimeSlot().isAfter(booking.getFinalTimeSlot())) {
            throw new BookingLabException(BookingLabException.INITDATE_MAYOR_ENDDATE);
        }
        if (!bookingRepository
                .findByInitialTimeSlotAndFinalTimeSlot(booking.getInitialTimeSlot(), booking.getFinalTimeSlot())
                .isEmpty()) {
            throw new BookingLabException(BookingLabException.DATE_ALREADY_TAKEN);
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

    public Optional<Booking> getReservationById(Long id) {
        return bookingRepository.findById(id);
    }

    public Optional<Booking> getReservationByProfessor(String professorName) {
        return bookingRepository.findByTeacher(professorName.toUpperCase());
    }

    /* public Optional<Booking> getReservationByLaboratory(String labname) {
        return reservationRepository.findByBLaboratory(labname);
    } */

    public Booking updateReservation(Booking booking) throws BookingLabException {
        if (bookingRepository.findById(booking.getBookingId()).isEmpty()) {
            throw new BookingLabException(BookingLabException.RESERVATION_NOT_FOUND);
        }
        Optional<Booking> conflictingReservation = bookingRepository.findByInitialTimeSlotAndFinalTimeSlot(
                booking.getInitialTimeSlot(),
                booking.getFinalTimeSlot()
        );

        if (conflictingReservation.isPresent() && !conflictingReservation.get().getBookingId().equals(booking.getBookingId())) {
            throw new BookingLabException(BookingLabException.DATE_ALREADY_TAKEN);
        }

        booking = nameFormatter(booking);
        return bookingRepository.save(booking);
    }

    public void delReservation(Booking booking){
        bookingRepository.deleteById(booking.getBookingId());
    }

    public List<String> getWeekDays() {
        return Day.getAllDays();
    }

    public List<String> getLaboratories() {
        return Laboratory.getAllLaboratories();
    }

}
