package edu.eci.labinfo.bookinglab.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.eci.labinfo.bookinglab.model.Reservation;
import edu.eci.labinfo.bookinglab.service.ReservationService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Data;

@Component
@ViewScoped
@Data
public class BookingController {

    private ScheduleModel eventModel;
    private String serverTimeZone;
    private ScheduleEvent<?> event;
    private static LocalDate date = LocalDate.now();
    private static LocalTime timeam = LocalTime.of(7,0);
    private static LocalTime timepm = LocalTime.of(19,0);
    public  final LocalDateTime mindate = LocalDateTime.of(date, timeam);
    public  final LocalDateTime maxdate = LocalDateTime.of(date, timepm);
    Logger logger;

    @Autowired
    private ReservationService reservationService;

    private Reservation reservation;

    @PostConstruct
    public void init() {
        serverTimeZone = ZoneId.systemDefault().toString();
        eventModel = new DefaultScheduleModel();
        event = new DefaultScheduleEvent<>();
        logger = LoggerFactory.getLogger(BookingController.class);
        reservation = new Reservation();
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

    public void saveReservation() {
        logger.info("Professor name: " + reservation.getProfessor());
        logger.info("Reserva guardada");
        // TODO implementar logica para guardar la reserva
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        String redirectPath = "index.xhtml";
        try {
            ec.redirect(ec.getRequestContextPath() + redirectPath);
        } catch (IOException e) {
            logger.error("Error al redirigir a la pagina de inicio");
        }
    }

}
