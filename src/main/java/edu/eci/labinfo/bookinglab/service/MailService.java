package edu.eci.labinfo.bookinglab.service;

import edu.eci.labinfo.bookinglab.model.Booking;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase que define los servicios de envio de correos
 *
 * @author Daniel Antonio Santanilla
 * @version 1.0
 */
@Service
public class MailService {

    private static final String RESERVA_DE_LABORATORIO = "Reserva de laboratorio";
    private final JavaMailSender javaMailSender;
    private final Configuration mailConfig;
    private final Logger logger;
    @Value("${spring.mail.username}")
    private String from;

    public MailService(JavaMailSender javaMailSender, Configuration mailConfig) {
        this.javaMailSender = javaMailSender;
        this.mailConfig = mailConfig;
        logger = LoggerFactory.getLogger(MailService.class);
    }

    /**
     * Envia un correo con las reservas
     *
     * @param to       Correo al que se le enviara el mensaje
     * @param bookings Reservas a enviar
     */
    public Boolean sendMail(String to, List<Booking> bookings) {
        Map<String, Object> model = mappingBooking(bookings);
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            Template template = mailConfig.getTemplate("email-template.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            helper.setSubject(RESERVA_DE_LABORATORIO);
            helper.setTo(to);
            helper.setFrom(from);
            helper.setText(html, true);
            javaMailSender.send(message);
        } catch (MessagingException | IOException | TemplateException | MailException e) {
            logger.error("Error al enviar el correo {}", e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Mapea las reservas para enviarlas en el correo con la plantilla
     *
     * @param bookings Reservas a mapear
     * @return Mapa con las reservas
     */
    private Map<String, Object> mappingBooking(List<Booking> bookings) {
        Map<String, Object> bookingMap = new HashMap<>();
        StringBuilder dates = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        for (Booking booking : bookings) {
            bookingMap.put("teacher", booking.getTeacher());
            bookingMap.put("course", booking.getCourse());
            bookingMap.put("laboratory", booking.getLaboratory());
            bookingMap.put("initialTimeSlot", booking.getInitialTimeSlot().format(formatter));
            bookingMap.put("finalTimeSlot", booking.getFinalTimeSlot().format(formatter));
            dates.append("<br>");
            dates.append(booking.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        bookingMap.put("date", dates);
        return bookingMap;
    }


}
