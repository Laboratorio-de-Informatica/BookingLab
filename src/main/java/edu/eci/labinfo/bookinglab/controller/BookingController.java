package edu.eci.labinfo.bookinglab.controller;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import edu.eci.labinfo.bookinglab.model.Booking;
import edu.eci.labinfo.bookinglab.model.BookingLabException;
import edu.eci.labinfo.bookinglab.model.Laboratory;
import edu.eci.labinfo.bookinglab.service.BookingService;
import edu.eci.labinfo.bookinglab.service.PrimeFacesWrapper;
import edu.eci.labinfo.bookinglab.utils.LabColorManager;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import lombok.Data;

@Component
@SessionScope
@Data
public class BookingController {

    private ScheduleModel eventModel;
    private String serverTimeZone;
    private ScheduleEvent<Booking> event;
    private LocalTime minTime;
    private LocalTime maxTime;
    private List<String> laboratories;
    private Booking booking;
    Logger logger;

    @Autowired
    BookingService bookingService;
    @Autowired
    DurationController durationController;
    @Autowired
    private PrimeFacesWrapper primeFacesWrapper;

    private static final String FORM_MESSAGES = "form:messages";
    private static final String ERROR = "Error";

    @PostConstruct
    public void init() {
        laboratories = bookingService.getLaboratories();
        minTime = LocalTime.of(7, 0);
        maxTime = LocalTime.of(19, 0);
        serverTimeZone = ZoneId.systemDefault().toString();
        eventModel = new DefaultScheduleModel();
        event = new DefaultScheduleEvent<>();
        logger = LoggerFactory.getLogger(BookingController.class);
    }

    private void loadReservations() {
        List<Booking> bookings = bookingService.getAllReservations();
        for (Booking booking : bookings) {
            placeBookingEvent(booking);
        }
    }

    private void placeBookingEvent(Booking booking) {
        LocalDateTime startDateTime = LocalDateTime.of(booking.getDate(), booking.getInitialTimeSlot());
        LocalDateTime endDateTime = LocalDateTime.of(booking.getDate(), booking.getFinalTimeSlot());
        DefaultScheduleEvent<?> event = DefaultScheduleEvent.builder()
                .title(booking.getCourse().toUpperCase() + " - " + booking.getTeacher() + " (" + booking.getLaboratory() + ")")
                .startDate(startDateTime)
                .endDate(endDateTime)
                .description(booking.getObservation())
                .data(booking)
                .overlapAllowed(true)
                .resizable(false)
                .draggable(false)
                .backgroundColor(LabColorManager.getInstance().getColor(Laboratory.findByValue(booking.getLaboratory())))
                .build();
        eventModel.addEvent(event);
    }

    public void startBooking() {
        logger.info("Iniciando reserva");
        this.booking = new Booking();
    }

    public void onEventSelect(SelectEvent<ScheduleEvent<Booking>> selectEvent) {
        event = selectEvent.getObject();
        booking = event.getData();
        logger.info("Evento seleccionado");
    }

    public Boolean onEventUpdate() {
        try {
            booking.setInitialTimeSlot(event.getStartDate().toLocalTime());
            booking.setFinalTimeSlot(event.getEndDate().toLocalTime());
            booking.setDate(event.getStartDate().toLocalDate());
            eventModel.deleteEvent(event);
            placeBookingEvent(booking);
            bookingService.updateReservation(booking);
            logger.info("Evento " + booking.getBookingId() + " actualizado");
        } catch (BookingLabException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, ERROR, e.getMessage()));
            primeFacesWrapper.current().ajax().update(FORM_MESSAGES);
            return false;
        }
        return true;
    }

    public Boolean onEventCancel() {
        try {
            Booking bookingToCancel = event.getData();
            bookingToCancel.setCanceled(true);
            bookingService.updateReservation(bookingToCancel);
            logger.info("Evento " + bookingToCancel.getBookingId() + " cancelado");
        } catch (BookingLabException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, ERROR, e.getMessage()));
            primeFacesWrapper.current().ajax().update(FORM_MESSAGES);
            return false;
        }
        return true;
    }

    public Boolean onEventDelete() {
        Booking bookingToDelete = (Booking) event.getData();
        bookingToDelete.setCanceled(true);
        bookingService.deleteReservation(bookingToDelete);
        logger.info("Evento " + bookingToDelete.getBookingId() + " eliminado");
        eventModel.deleteEvent(event);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Evento eliminado"));
        primeFacesWrapper.current().ajax().update(FORM_MESSAGES);
        return true;
    }

    public Boolean saveReservation() {
        List<DayOfWeek> selectedDays = durationController.getSelectedDays();
        int repetitions = durationController.getRepetitions();
        int duration = durationController.getDuration();
        for (int i = 0; i < duration; i++) {
            for (DayOfWeek day : selectedDays) {
                try {
                    Booking bookingToSave = makeBooking(repetitions, i, day);
                    bookingService.createReservation(bookingToSave);
                    placeBookingEvent(bookingToSave);
                } catch (BookingLabException e) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, ERROR, e.getMessage()));
                    primeFacesWrapper.current().ajax().update(FORM_MESSAGES);
                    return false;
                }
            }
        }

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        String redirectPath = "index.xhtml";
        try {
            ec.redirect(ec.getRequestContextPath() + redirectPath);
        } catch (IOException e) {
            logger.error(BookingLabException.ERRO_INDEX_PAGE);
            return false;
        }
        return true;
    }

    private Booking makeBooking(int repetitions, int i, DayOfWeek day) {
        Booking bookingToSave = new Booking();
        bookingToSave.setTeacher(this.booking.getTeacher());
        bookingToSave.setCourse(this.booking.getCourse());
        bookingToSave.setLaboratory(this.booking.getLaboratory());
        bookingToSave.setInitialTimeSlot(this.booking.getInitialTimeSlot());
        bookingToSave.setFinalTimeSlot(this.booking.getFinalTimeSlot());
        bookingToSave.setObservation(this.booking.getObservation());
        bookingToSave.setCanceled(false);
        bookingToSave.setDay(day);
        bookingToSave.setDate(LocalDate.now().with(day));
        bookingToSave.setDate(bookingToSave.getDate().plusWeeks(repetitions * i));
        return bookingToSave;
    }

}
