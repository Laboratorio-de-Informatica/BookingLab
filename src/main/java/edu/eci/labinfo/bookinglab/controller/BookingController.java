package edu.eci.labinfo.bookinglab.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;


import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.fasterxml.jackson.databind.JsonSerializable.Base;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;

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
import jakarta.servlet.http.HttpServletResponse;
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
    private Boolean firstLoad;
    
    private String selectedLaboratory;
    private String teacherToSearch;
    private String courseToSearch;
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
        firstLoad = true;
        minTime = LocalTime.of(7, 0);
        maxTime = LocalTime.of(19, 0);
        serverTimeZone = ZoneId.systemDefault().toString();
        eventModel = new DefaultScheduleModel();
        event = new DefaultScheduleEvent<>();
        logger = LoggerFactory.getLogger(BookingController.class);
    }

    public void loadReservationsDB() {
        if (firstLoad) {
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
        System.out.println(booking);
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

    public Boolean onLaboratorySelect() {
        logger.info("Laboratorio seleccionado " + selectedLaboratory);
        eventModel.clear();
        List<Booking> bookings = bookingService.getReservationByLaboratory(selectedLaboratory);
        selectedLaboratory = null;
        if (bookings.size() > 0) {
            for (Booking booking : bookings) {
                placeBookingEvent(booking);
            }
            return true;
        }
        return false;
    }

    public Boolean onTeacherSearch() {
        logger.info("Profesor a buscar " + teacherToSearch);
        eventModel.clear();
        List<Booking> bookings = bookingService.getReservationByTeacher(teacherToSearch);
        teacherToSearch = null;
        if (bookings.size() > 0) {
            for (Booking booking : bookings) {
                placeBookingEvent(booking);
            }
            return true;
        }
        return false;
    }
    
    public Boolean onCourseSearch() {
        logger.info("Curso a buscar " + courseToSearch);
        eventModel.clear();
        List<Booking> bookings = bookingService.getReservationByCourse(courseToSearch);
        courseToSearch = null;
        if (bookings.size() > 0) {
            for (Booking booking : bookings) {
                placeBookingEvent(booking);
            }
            return true;
        }
        return false;
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



    public void exportToPDF() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY)); // Cambiado a sábado
    
        for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)) {
            try {
                String fileName = "schedule_" + date.getDayOfWeek().toString().toLowerCase() + ".pdf";
                String directoryPath = "C:/Users/rescate/Downloads"; // Cambia esto por la ruta de tu directorio de destino
                String filePath = Paths.get(directoryPath, fileName).toString();

                // Ruta de la plantilla PDF
                String templatePath = "C:/Users/rescate/Downloads/plantilla_semanal_horario.pdf"; // Cambia esto por la ruta de tu plantilla PDF

                // Cargar la plantilla PDF
                PdfReader reader = new PdfReader(templatePath);
                Rectangle pageSize = reader.getPageSize(1);

                // Crear el documento PDF
                Document document = new Document(pageSize);
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();

                PdfImportedPage page = writer.getImportedPage(reader, 1);
                // Agregar la página de la plantilla al nuevo documento
                PdfContentByte contentByte = writer.getDirectContent();
                contentByte.addTemplate(page, 0, 0);


                // Crear y agregar la tabla al documento
                PdfPTable table = createScheduleTable(date);

                // Escribir la tabla en el documento
                table.writeSelectedRows(0, -1, 10, 650, contentByte);
                
                Paragraph title = new Paragraph("Día de la semana: " + date.getDayOfWeek().toString(), FontFactory.getFont("Bebas Neue", 30, Font.BOLD));
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);

                // Cerrar el documento
                document.close();
                reader.close();

                System.out.println("PDF guardado en: " + filePath);
            } catch (DocumentException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private PdfPTable createScheduleTable(LocalDate date) throws DocumentException {
        // Obtener las reservas para el día específico
        List<Booking> bookings = bookingService.getReservationByDay(date.getDayOfWeek());

        // Crear la tabla con las reservas para ese día
        PdfPTable table = new PdfPTable(laboratories.size() + 1); // Una columna adicional para las horas
        table.setWidthPercentage(100); // Ancho de la tabla al 100% de la página
    
        // Establecer anchos personalizados para las columnas
        float[] columnWidths = new float[laboratories.size() + 1];
        columnWidths[0] = 1.2F; // Anchura de la primera columna, en 1. fraccion
        for (int i = 1; i < columnWidths.length; i++) {
            columnWidths[i] = 1F; // Anchura de las columnas de las horas, en 1 fraccion
        }
        table.setWidths(columnWidths);
        table.setTotalWidth(1400F);
    
        // Establecer estilos para las celdas de los laboratorios y horas
        PdfPCell headerCell = createCell(new BaseColor(88, 182, 88), new BaseColor(191,191,191), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 56F, 7.5F);
        Font headerFont = FontFactory.getFont("Bebas Neue", 25, Font.BOLD, BaseColor.WHITE); // Fuente "Bebas Neue", tamaño 28, color blanco, negrita
        headerCell.setPhrase(new Phrase("Hora", headerFont));
        table.addCell(headerCell);
    
        for (LocalTime currentTime = LocalTime.of(7, 0); currentTime.isBefore(LocalTime.of(19, 0)); currentTime = currentTime.plusMinutes(90)) {
            PdfPCell timeCell = createCell(new BaseColor(88, 182, 88), new BaseColor(191,191,191), Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 56F, 7.5F);
            Font timeFont = FontFactory.getFont("Bebas Neue", 25, Font.BOLD, BaseColor.WHITE); // Fuente "Bebas Neue", tamaño 28, color blanco, negrita
            timeCell.setPhrase(new Phrase(currentTime.toString(), timeFont));
            table.addCell(timeCell);
        }
    
        // Establecer estilos para las celdas de los datos de las reservas
        int i = 0;
        for (String laboratory : laboratories) {
            PdfPCell labCell = createCell(new BaseColor(88, 182, 88), new BaseColor(191,191,191), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 56F, 7.5F);
            Font labFont = FontFactory.getFont("Bebas Neue", 25, Font.BOLD, BaseColor.WHITE); // Fuente "Bebas Neue", tamaño 28, color blanco, negrita
            labCell.setPhrase(new Phrase(laboratory, labFont));
            table.addCell(labCell);
            
            
            for (LocalTime currentTime = LocalTime.of(7, 0); currentTime.isBefore(LocalTime.of(19, 0)); currentTime = currentTime.plusMinutes(90)) {
                String bookingInfo = getBookingInfo(currentTime, laboratory, bookings);
                PdfPCell infoCell;
                if (i % 2 == 0 ) {
                    infoCell = createCell(new BaseColor(213, 227, 207), new BaseColor(191,191,191), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 56F, 7.5F);
                } else {
                    infoCell = createCell(new BaseColor(235, 241, 233), new BaseColor(191,191,191), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 56F, 7.5F);
                }
                Font infoFont = FontFactory.getFont("Calibri", 20, BaseColor.BLACK); // Fuente "Bebas Neue", tamaño 28, color blanco, negrita
                infoCell.setPhrase(new Phrase(bookingInfo, infoFont));
                table.addCell(infoCell);
            }
            i++;
        }
    
        return table;
    }

    private PdfPCell createCell(BaseColor bgColor, BaseColor borderColor, int horizontalAlignment, int verticalAlignment, float minHeight, float padding) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(bgColor);
        cell.setBorderColor(borderColor);
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setVerticalAlignment(verticalAlignment);
        cell.setMinimumHeight(minHeight);
        cell.setPadding(padding);
        return cell;
    }
    
    private String getBookingInfo(LocalTime time, String laboratory, List<Booking> bookings) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
    
        StringBuilder bookingInfo = new StringBuilder();
    
        for (Booking booking : bookings) {
            LocalDateTime startDateTime = LocalDateTime.of(booking.getDate(), booking.getInitialTimeSlot());
    
            // Verificar si la reserva está en el laboratorio y hora específicos
            // y si está dentro de la semana actual
            if (booking.getLaboratory().equals(laboratory) &&
                startDateTime.toLocalTime().equals(time) &&
                startDateTime.toLocalDate().isAfter(startOfWeek.minusDays(1)) && // Incluye el inicio del lunes
                startDateTime.toLocalDate().isBefore(endOfWeek.plusDays(1))) { // Incluye el final del domingo
                bookingInfo.append(booking.getCourse()).append("-").append(booking.getTeacher());
                bookingInfo.append("\n"); // Agregar un salto de línea para separar las reservas
            }
        }
        return bookingInfo.toString();
    }
    


}
