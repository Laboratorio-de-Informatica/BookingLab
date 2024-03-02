package edu.eci.labinfo.bookinglab.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import edu.eci.labinfo.bookinglab.model.Booking;

@Service
public class ScheduleExportService {

    public static final String BEBAS_NEUE = "Bebas Neue";
    public static final String CALIBRI = "Calibri";
    private final BookingService bookingService;
    private final Logger logger;
    private List<String> laboratories;

    public ScheduleExportService(BookingService bookingService) {
        this.bookingService = bookingService;
        laboratories = bookingService.getLaboratories();
        logger = LoggerFactory.getLogger(ScheduleExportService.class);
    }

    public ByteArrayInputStream exportToPDF(LocalDate date) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            String templatePath = "C:/Users/rescate/Downloads/plantilla_semanal_horario.pdf"; // Cambia esto por la ruta de tu plantilla PDF

            // Cargar la plantilla PDF
            PdfReader reader = new PdfReader(templatePath);
            Rectangle pageSize = reader.getPageSize(1);

            // Crear el documento PDF
            Document document = new Document(pageSize);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();

            PdfImportedPage page = writer.getImportedPage(reader, 1);
            // Agregar la página de la plantilla al nuevo documento
            PdfContentByte contentByte = writer.getDirectContent();
            contentByte.addTemplate(page, 0, 0);

            // Crear y agregar la tabla al documento
            PdfPTable table = createScheduleTable(date);

            // Escribir la tabla en el documento
            table.writeSelectedRows(0, -1, 10, 650, contentByte);

            Paragraph title = new Paragraph("Día de la semana: " + date.getDayOfWeek().toString(),
                    FontFactory.getFont(BEBAS_NEUE, 30, Font.BOLD));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Cerrar el documento
            document.close();
            reader.close();

            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (DocumentException | IOException e) {
            logger.error("Error al exportar el horario a PDF: {}", e.getMessage());
            return null;
        }
    }

    private PdfPTable createScheduleTable(LocalDate date) throws DocumentException {
        // Obtener las reservas para el día específico
        java.util.List<Booking> bookings = bookingService.getReservationByDay(date.getDayOfWeek());

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
        Font headerFont = FontFactory.getFont(BEBAS_NEUE, 25, Font.BOLD, BaseColor.WHITE); // Fuente "Bebas Neue", tamaño 28, color blanco, negrita
        headerCell.setPhrase(new Phrase("Hora", headerFont));
        table.addCell(headerCell);

        for (LocalTime currentTime = LocalTime.of(7, 0); currentTime.isBefore(LocalTime.of(19, 0)); currentTime = currentTime.plusMinutes(90)) {
            PdfPCell timeCell = createCell(new BaseColor(88, 182, 88), new BaseColor(191,191,191), Element.ALIGN_CENTER, Element.ALIGN_MIDDLE, 56F, 7.5F);
            Font timeFont = FontFactory.getFont(BEBAS_NEUE, 25, Font.BOLD, BaseColor.WHITE); // Fuente "Bebas Neue", tamaño 28, color blanco, negrita
            timeCell.setPhrase(new Phrase(currentTime.toString(), timeFont));
            table.addCell(timeCell);
        }

        // Establecer estilos para las celdas de los datos de las reservas
        int i = 0;
        for (String laboratory : laboratories) {
            PdfPCell labCell = createCell(new BaseColor(88, 182, 88), new BaseColor(191,191,191), Element.ALIGN_LEFT, Element.ALIGN_MIDDLE, 56F, 7.5F);
            Font labFont = FontFactory.getFont(BEBAS_NEUE, 25, Font.BOLD, BaseColor.WHITE); // Fuente "Bebas Neue", tamaño 28, color blanco, negrita
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
                Font infoFont = FontFactory.getFont(CALIBRI, 20, BaseColor.BLACK); // Fuente "Bebas Neue", tamaño 28, color blanco, negrita
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
