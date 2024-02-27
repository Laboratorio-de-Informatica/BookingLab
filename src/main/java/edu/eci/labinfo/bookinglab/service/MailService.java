package edu.eci.labinfo.bookinglab.service;

import edu.eci.labinfo.bookinglab.model.Booking;
import freemarker.template.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class MailService {

    private final JavaMailSender javaMailSender;
    private final Configuration mailConfig;
    private Logger logger;

    public MailService(JavaMailSender javaMailSender, Configuration mailConfig) {
        this.javaMailSender = javaMailSender;
        this.mailConfig = mailConfig;
        logger = LoggerFactory.getLogger(MailService.class);
    }

    public void sendMail(String to, String subject, Booking booking) {
        Map<String, Object> model = mappingBooking(booking);
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            Template template = mailConfig.getTemplate("email-template.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            javaMailSender.send(message);
        } catch (MessagingException | IOException | TemplateException e) {
            logger.error("Error al enviar el correo ".concat(e.getMessage()));
        }
    }

    private Map<String, Object> mappingBooking(Booking booking) {
        Map<String, Object> bookingMap = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        bookingMap.put("teacher", booking.getTeacher());
        bookingMap.put("course", booking.getCourse());
        bookingMap.put("date", booking.getDate());
        bookingMap.put("laboratory", booking.getLaboratory());
        bookingMap.put("initialTimeSlot", booking.getInitialTimeSlot().format(formatter));
        bookingMap.put("finalTimeSlot", booking.getFinalTimeSlot().format(formatter));
        bookingMap.put("day", booking.getDay().getDisplayName(TextStyle.FULL, new Locale("es", "CO")));

        return bookingMap;
    }


}
