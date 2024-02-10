package edu.eci.labinfo.bookinglab.controller;

import java.io.IOException;
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
import edu.eci.labinfo.bookinglab.service.BookingService;
import edu.eci.labinfo.bookinglab.service.PrimeFacesWrapper;
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
    private ScheduleEvent<?> event;
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
        loadReservations();
    }

    private void loadReservations() {
        List<Booking> bookings = bookingService.getAllReservations();
        for (Booking booking : bookings) {
            LocalDate today = LocalDate.now(); // Esto es solo un placeholder, ajusta según tu lógica
            LocalDateTime startDateTime = LocalDateTime.of(today, booking.getInitialTimeSlot());
            LocalDateTime endDateTime = LocalDateTime.of(today, booking.getFinalTimeSlot());

            DefaultScheduleEvent<?> event = DefaultScheduleEvent.builder()
                    .title(booking.getCourse() + " - " + booking.getTeacher() + " (" + booking.getLaboratory() + ")")
                    .startDate(startDateTime)
                    .endDate(endDateTime)
                    .description(booking.getObservation())
                    .data(booking.getBookingId())
                    .build();
            eventModel.addEvent(event);
        }
    }

    public void startBooking() {
        logger.info("Iniciando reserva");
        this.booking = new Booking();
        durationController.startDuration();
    }

    public void onEventSelect(SelectEvent<ScheduleEvent<?>> selectEvent) {
        event = selectEvent.getObject();
        logger.info("Evento seleccionado");
    }

    public void onDateSelect(SelectEvent<LocalDateTime> selectEvent) {
        event = DefaultScheduleEvent.builder()
                .startDate(selectEvent.getObject())
                .endDate(selectEvent.getObject().plusMinutes(90))
                .description("sleep as long as you want")
                .backgroundColor("#51A19F")
                .build();
        logger.info("Fecha seleccionada");
    }

    public void addEvent() {
        if (event.getId() == null) {
            eventModel.addEvent(event);
        } else {
            eventModel.updateEvent(event);
        }
        event = new DefaultScheduleEvent<>();
        logger.info("Evento agregado");
    }

    public void onEventDelete() {
        eventModel.deleteEvent(event);
        logger.info("Evento eliminado");
    }

    public Boolean saveReservation() {
        try {
            bookingService.createReservation(booking);
        } catch (BookingLabException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, ERROR, e.getMessage()));
            primeFacesWrapper.current().ajax().update(FORM_MESSAGES);
            return false;
        }
        // TODO: Ver como asignar multiples series
        if (durationController.getSelectedOption() != 0) {
            try {
                bookingService.updateReservation(booking);
            } catch (BookingLabException e) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, ERROR, e.getMessage()));
                primeFacesWrapper.current().ajax().update(FORM_MESSAGES);
                return false;
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

}
