package edu.eci.labinfo.bookinglab.service;

import org.springframework.beans.factory.annotation.Autowired;

import edu.eci.labinfo.bookinglab.data.ReservationRepository;
import edu.eci.labinfo.bookinglab.model.BookingLabException;
import edu.eci.labinfo.bookinglab.model.Reservation;

public class ReservationService {

    @Autowired
    private static ReservationRepository reservationRepository;

    public Reservation createReservation(Reservation reservation) throws BookingLabException {
        if (reservation.getInitialDateTime().isAfter(reservation.getEndDateTime())) {
            throw new BookingLabException(BookingLabException.INITDATE_MAYOR_ENDDATE);
        }
        if (!reservationRepository
                .findByInitialDateTimeAndEndDateTime(reservation.getInitialDateTime(), reservation.getEndDateTime())
                .isEmpty()) {
            throw new BookingLabException(BookingLabException.DATE_ALREADY_TAKEN);
        }
        return reservationRepository.save(reservation);
    }
}
