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
import org.springframework.web.context.annotation.RequestScope;

import edu.eci.labinfo.bookinglab.service.ScheduleExportService;
import jakarta.inject.Named;

@Named
@RequestScope
public class FileDownload {

    private StreamedContent file;
    private final ScheduleExportService scheduleExportService;

    public FileDownload(ScheduleExportService scheduleExportService) {
        this.scheduleExportService = scheduleExportService;
    }

    public void  getZippedFiles() {
        try {
            LocalDate today = LocalDate.now();
            LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

            // Crear un archivo ZIP temporal
            Path zipFilePath = Files.createTempFile("schedules", ".zip");
            FileOutputStream fos = new FileOutputStream(zipFilePath.toFile());
            ZipOutputStream zos = new ZipOutputStream(fos);

            // Generar y agregar cada archivo PDF al archivo ZIP
            for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)) {
                String fileName = "schedule_" + date.getDayOfWeek().toString().toLowerCase() + ".pdf";
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
