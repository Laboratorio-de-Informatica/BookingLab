package edu.eci.labinfo.bookinglab.service;

import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.eci.labinfo.bookinglab.data.BookingRepository;
import edu.eci.labinfo.bookinglab.model.Booking;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    public Booking createBooking(Long bookingId) {
        Booking booking = new Booking();
        booking.setBooking_id(bookingId);
        booking.setCalendar(new GregorianCalendar());
        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId).orElse(null);
    }

    public Booking updateBooking(Long bookingId, Booking booking) {
        Booking existingBooking = getBookingById(bookingId);
        if (existingBooking != null) {
            existingBooking.setReservations(booking.getReservations());
            existingBooking.setCalendar(booking.getCalendar());
            return bookingRepository.save(existingBooking);
        }
        return null;
    }

    public void deleteBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }
    
}
