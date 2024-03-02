package edu.eci.labinfo.bookinglab.controller;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import edu.eci.labinfo.bookinglab.model.Booking;
import edu.eci.labinfo.bookinglab.model.BookingLabException;
import edu.eci.labinfo.bookinglab.model.Laboratory;
import edu.eci.labinfo.bookinglab.service.BookingService;
import edu.eci.labinfo.bookinglab.service.MailService;
import edu.eci.labinfo.bookinglab.service.PrimeFacesWrapper;
import edu.eci.labinfo.bookinglab.service.ScheduleExportService;
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
    private Booking selectedBooking;
    private Boolean firstLoad;
    private Boolean sendEmail;
    private String selectedLaboratory;
    private String teacherEmail;
    private String teacherToSearch;
    private String courseToSearch;
    private Logger logger;

    private final BookingService bookingService;
    private final DurationController durationController;
    private final MailService mailService;
    private final PrimeFacesWrapper primeFacesWrapper;
    private final ScheduleExportService scheduleExportService;

    private static final String FORM_MESSAGES = "form:messages";
    private static final String ERROR = "Error";

    public BookingController(BookingService bookingService,
                             DurationController durationController,
                             MailService mailService,
                             PrimeFacesWrapper primeFacesWrapper,
                             ScheduleExportService scheduleExportService) {
        this.bookingService = bookingService;
        this.durationController = durationController;
        this.mailService = mailService;
        this.primeFacesWrapper = primeFacesWrapper;
        this.scheduleExportService = scheduleExportService;
    }

    @PostConstruct
    public void init() {
        laboratories = bookingService.getLaboratories();
        sendEmail = false;
        firstLoad = true;
        minTime = LocalTime.of(7, 0);
        maxTime = LocalTime.of(19, 0);
        serverTimeZone = ZoneId.systemDefault().toString();
        eventModel = new DefaultScheduleModel();
        event = new DefaultScheduleEvent<>();
        logger = LoggerFactory.getLogger(BookingController.class);
    }

    public void loadReservationsDB() {
        if (Boolean.TRUE.equals(firstLoad)) {
            loadReservations();
            firstLoad = false;
        }
    }

    public void loadReservations() {
        eventModel.clear();
        List<Booking> bookings = bookingService.getAllReservations();
        logger.info("Cargando reservas");
        for (Booking booking : bookings) {
            placeBookingEvent(booking);
        }
        primeFacesWrapper.current().ajax().update("form:schedule");
    }

    private void placeBookingEvent(Booking booking) {
        LocalDateTime startDateTime = LocalDateTime.of(booking.getDate(), booking.getInitialTimeSlot());
        LocalDateTime endDateTime = LocalDateTime.of(booking.getDate(), booking.getFinalTimeSlot());
        DefaultScheduleEvent<?> bookingEvent = DefaultScheduleEvent.builder()
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
        eventModel.addEvent(bookingEvent);
    }

    public void startBooking() {
        logger.info("Iniciando reserva");
        this.selectedBooking = new Booking();
    }

    public void onEventSelect(SelectEvent<ScheduleEvent<Booking>> selectEvent) {
        event = selectEvent.getObject();
        selectedBooking = event.getData();
        logger.info("Evento seleccionado");
    }

    public Boolean onEventUpdate() {
        try {
            selectedBooking.setInitialTimeSlot(event.getStartDate().toLocalTime());
            selectedBooking.setFinalTimeSlot(event.getEndDate().toLocalTime());
            selectedBooking.setDate(event.getStartDate().toLocalDate());
            eventModel.deleteEvent(event);
            placeBookingEvent(selectedBooking);
            bookingService.updateReservation(selectedBooking);
            logger.info("Evento {} actualizado", selectedBooking.getBookingId());
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
            logger.info("Evento {} cancelado", bookingToCancel.getBookingId());
        } catch (BookingLabException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, ERROR, e.getMessage()));
            primeFacesWrapper.current().ajax().update(FORM_MESSAGES);
            return false;
        }
        return true;
    }

    public Boolean onLaboratorySelect() {
        logger.info("Laboratorio seleccionado {}", selectedLaboratory);
        eventModel.clear();
        List<Booking> bookings = bookingService.getReservationByLaboratory(selectedLaboratory);
        selectedLaboratory = null;
        if (!bookings.isEmpty()) {
            for (Booking booking : bookings) {
                placeBookingEvent(booking);
            }
            return true;
        }
        return false;
    }

    public Boolean onTeacherSearch() {
        logger.info("Profesor a buscar: {}", teacherToSearch);
        eventModel.clear();
        List<Booking> bookings = bookingService.getReservationByTeacher(teacherToSearch);
        teacherToSearch = null;
        if (!bookings.isEmpty()) {
            for (Booking booking : bookings) {
                placeBookingEvent(booking);
            }
            return true;
        }
        return false;
    }
    
    public Boolean onCourseSearch() {
        logger.info("Curso a buscar: {}", courseToSearch);
        eventModel.clear();
        List<Booking> bookings = bookingService.getReservationByCourse(courseToSearch);
        courseToSearch = null;
        if (!bookings.isEmpty()) {
            for (Booking booking : bookings) {
                placeBookingEvent(booking);
            }
            return true;
        }
        return false;
    }

    public Boolean onEventDelete() {
        Booking bookingToDelete = event.getData();
        bookingToDelete.setCanceled(true);
        bookingService.deleteReservation(bookingToDelete);
        logger.info("Evento {} eliminado", bookingToDelete.getBookingId());
        eventModel.deleteEvent(event);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Evento eliminado"));
        primeFacesWrapper.current().ajax().update(FORM_MESSAGES);
        return true;
    }

    public Boolean saveReservation() {
        List<DayOfWeek> selectedDays = durationController.getSelectedDays();
        List<Booking> toSend = new ArrayList<>();
        int repetitions = durationController.getRepetitions();
        int duration = durationController.getDuration();
        for (int i = 0; i < duration; i++) {
            for (DayOfWeek day : selectedDays) {
                try {
                    Booking bookingToSave = makeBooking(repetitions, i, day);
                    bookingService.createReservation(bookingToSave);
                    placeBookingEvent(bookingToSave);
                    toSend.add(bookingToSave);
                } catch (BookingLabException e) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, ERROR, e.getMessage()));
                    primeFacesWrapper.current().ajax().update(FORM_MESSAGES);
                    return false;
                }
            }
        }
        if (Boolean.TRUE.equals(sendEmail)) {
            mailService.sendMail(teacherEmail, toSend);
            logger.info("Correo enviado a {}", teacherEmail);
            teacherEmail = "";
            sendEmail = false;
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
        bookingToSave.setTeacher(this.selectedBooking.getTeacher());
        bookingToSave.setCourse(this.selectedBooking.getCourse());
        bookingToSave.setLaboratory(this.selectedBooking.getLaboratory());
        bookingToSave.setInitialTimeSlot(this.selectedBooking.getInitialTimeSlot());
        bookingToSave.setFinalTimeSlot(this.selectedBooking.getFinalTimeSlot());
        bookingToSave.setObservation(this.selectedBooking.getObservation());
        bookingToSave.setCanceled(false);
        bookingToSave.setDay(day);
        bookingToSave.setDate(LocalDate.now().with(day));
        bookingToSave.setDate(bookingToSave.getDate().plusWeeks(repetitions * i));
        return bookingToSave;
    }

    public void scheduleExport() {
        scheduleExportService.exportToPDF();
    }

}
