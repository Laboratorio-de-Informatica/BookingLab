package edu.eci.labinfo.bookinglab.service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import edu.eci.labinfo.bookinglab.data.BookingRepository;
import edu.eci.labinfo.bookinglab.model.Booking;
import edu.eci.labinfo.bookinglab.model.BookingLabException;
import edu.eci.labinfo.bookinglab.model.Laboratory;

/**
 * Clase que define los servicios de las reservas
 * @version 1.0
 * @author Daniel Antonio Santanilla
 * @author Andres Camilo Oniate
 */
@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    /**
     * Crea una reserva
     * @param booking Reserva a crear
     * @return Reserva creada
     * @throws BookingLabException Si la fecha inicial es mayor a la fecha final
     */
    public Booking createReservation(Booking booking) throws BookingLabException {
        if (booking.getInitialTimeSlot().isAfter(booking.getFinalTimeSlot())) {
            throw new BookingLabException(BookingLabException.INITDATE_MAYOR_ENDDATE);
        }
        return bookingRepository.save(nameFormatter(booking));
    }

    /**
     * Formatea el nombre del profesor a mayusculas
     * @param booking Reserva a formatear
     * @return Reserva con el nombre del profesor en mayusculas
     */
    private Booking nameFormatter(Booking booking) {
        String name = booking.getTeacher();
        booking.setTeacher(name.toUpperCase());
        return booking;
    }

    /**
     * Obtiene todas las reservas
     * @return Todas las reservas
     */
    public List<Booking> getAllReservations() {
        return bookingRepository.findAll();
    }

    /**
     * Obtiene una reserva por su id
     * @param id Id de la reserva
     * @return Reserva encontrada
     * @throws BookingLabException Si la reserva no es encontrada
     */
    public Optional<Booking> getReservationById(Long id) throws BookingLabException {
        if (bookingRepository.findById(id).isEmpty()) {
            throw new BookingLabException(BookingLabException.RESERVATION_NOT_FOUND);
        }
        return bookingRepository.findById(id);
    }

    /**
     * Obtiene las reservas de un profesor
     * @param teacher Nombre del profesor
     * @return Reservas del profesor
     */
    public List<Booking> getReservationByTeacher(String teacher) {
        return bookingRepository.findByTeacher(teacher.toUpperCase());
    }

    /**
     * Obtiene las reservas de un laboratorio
     * @param laboratory Nombre del laboratorio
     * @return Reservas del laboratorio
     */
    public List<Booking> getReservationByLaboratory(String laboratory) {
        return bookingRepository.findByLaboratory(laboratory);
    }

    /**
     * Obtiene las reservas de un curso
     * @param course Nombre del curso
     * @return Reservas del curso
     */
    public List<Booking> getReservationByCourse(String course) {
        return bookingRepository.findByCourse(course);
    }

    /**
     * Actualiza una reserva
     * @param booking Reserva a actualizar
     * @return Reserva actualizada
     * @throws BookingLabException Si la reserva no es encontrada
     */
    public Booking updateReservation(Booking booking) throws BookingLabException {
        if (bookingRepository.findById(booking.getBookingId()).isEmpty()) {
            throw new BookingLabException(BookingLabException.RESERVATION_NOT_FOUND);
        }
        booking = nameFormatter(booking);
        return bookingRepository.save(booking);
    }

    /**
     * Elimina una reserva
     * @param booking Reserva a eliminar
     */
    public void deleteReservation(Booking booking) {
        bookingRepository.deleteById(booking.getBookingId());
    }

    /**
     * Obtiene los dias de la semana
     * @return Dias de la semana
     */
    public DayOfWeek[] getWeekDays() {
        return DayOfWeek.values();
    }

    /**
     * Obtiene los laboratorios
     * @return Lista de laboratorios
     */
    public List<String> getLaboratories() {
        return Laboratory.getAllLaboratories();
    }

    /**
     * Obtiene las reservas de un dia
     * @param day Dia de la semana
     * @return Reservas del dia
     */
    public List<Booking> getReservationByDay(DayOfWeek day) {
        return bookingRepository.findByDay(day);
    }

}
