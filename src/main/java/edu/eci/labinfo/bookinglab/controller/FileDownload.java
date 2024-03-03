package edu.eci.labinfo.bookinglab.controller;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import edu.eci.labinfo.bookinglab.service.ScheduleExportService;

/**
 * Clase que permite descargar un archivo ZIP con los horarios de la semana
 * @version 1.0
 * @author Andres Camilo Oniate
 */
@Component
@RequestScope
public class FileDownload {

    private StreamedContent file;
    private final ScheduleExportService scheduleExportService;
    private Logger logger;

    public FileDownload(ScheduleExportService scheduleExportService) {
        this.scheduleExportService = scheduleExportService;
        logger = LoggerFactory.getLogger(FileDownload.class);
    }

    /**
     * Genera un archivo ZIP con los horarios de la semana
     * @return Archivo ZIP con los horarios de la semana
     */
    public void getZippedFiles() {
        try {
            LocalDate today = LocalDate.now();
            LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate endOfWeek = (today.getDayOfWeek() == DayOfWeek.SUNDAY)
                ? today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SATURDAY))
                : today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

            // Crear un archivo ZIP temporal
            Path zipFilePath = Files.createTempFile("schedules", ".zip");
            FileOutputStream fos = new FileOutputStream(zipFilePath.toFile());
            ZipOutputStream zos = new ZipOutputStream(fos);

            // Generar y agregar cada archivo PDF al archivo ZIP
            for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)) {

                String fileName = "schedule_" + date.getDayOfWeek().toString().toLowerCase() + ".pdf";
                logger.info("Generando archivo PDF {} {} {} {}", fileName, startOfWeek, endOfWeek, date);
                ByteArrayInputStream pdfStream = scheduleExportService.exportToPDF(date);

                ZipEntry zipEntry = new ZipEntry(fileName);
                zos.putNextEntry(zipEntry);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = pdfStream.read(buffer)) != -1) {
                    zos.write(buffer, 0, bytesRead);
                }

                zos.closeEntry();
                pdfStream.close();
            }

            zos.close();
            fos.close();

            // Convertir el archivo ZIP en un StreamedContent para descarga
            FileInputStream zipInputStream = new FileInputStream(zipFilePath.toFile());
            file = DefaultStreamedContent.builder()
                    .name("schedules.zip")
                    .contentType("application/zip")
                    .stream(() -> zipInputStream)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public StreamedContent getFile() {
        getZippedFiles();
        return file;
    }
    
}
