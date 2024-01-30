package edu.eci.labinfo.bookinglab.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.eci.labinfo.bookinglab.data.ReservationRepository;
import edu.eci.labinfo.bookinglab.model.BookingLabException;
import edu.eci.labinfo.bookinglab.model.Reservation;

@Service
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
        reservation = nameFormatter(reservation);
        return reservationRepository.save(reservation);
    }

    private Reservation nameFormatter(Reservation reservation) {
        String name = reservation.getProfessor();
        reservation.setProfessor(name.toUpperCase());
        return reservation;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    public Optional<Reservation> getReservationByProfessor(String professorName) {
        return reservationRepository.findByProfessor(professorName.toUpperCase());
    }

    /* public Optional<Reservation> getReservationByLaboratory(String labname) {
        return reservationRepository.findByBLaboratory(labname);
    } */

    public Reservation updateReservation(Reservation reservation) throws BookingLabException {
        if (reservationRepository.findById(reservation.getIdReservation()).isEmpty()) {
            throw new BookingLabException(BookingLabException.RESERVATION_NOT_FOUND);
        }
        Optional<Reservation> conflictingReservation = reservationRepository.findByInitialDateTimeAndEndDateTime(
                reservation.getInitialDateTime(),
                reservation.getEndDateTime()
        );

        if (conflictingReservation.isPresent() && !conflictingReservation.get().getIdReservation().equals(reservation.getIdReservation())) {
            throw new BookingLabException(BookingLabException.DATE_ALREADY_TAKEN);
        }

        reservation = nameFormatter(reservation);
        return reservationRepository.save(reservation);
    }

    public void delReservation(Reservation reservation){
        reservationRepository.deleteById(reservation.getIdReservation());
    }

}
