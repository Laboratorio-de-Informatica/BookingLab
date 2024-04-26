package edu.eci.labinfo.bookinglab.controller;

import edu.eci.labinfo.bookinglab.service.BookingService;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Clase que controla la duracion de la reserva
 *
 * @author Daniel Antonio Santanilla
 * @version 1.0
 */
@Component
@SessionScope
@Data
public class DurationController {

    private int selectedOption;
    private String summaryMessage;
    private DayOfWeek[] days;
    private List<DayOfWeek> selectedDays;
    private int repetitions;
    private int duration;
    private Locale colombianLocale;
    private Logger logger;
    private final BookingService bookingService;

    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(DurationController.class);
        selectedOption = 0;
        repetitions = 1;
        duration = 1;
        colombianLocale = new Locale("es", "CO");
        summaryMessage = "no se repite";
        days = DayOfWeek.values();
        selectedDays = new ArrayList<>();
        updateSummaryMessage();
    }

    public DurationController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Nombre del dia de la semana
     *
     * @param day Dia de la semana
     * @return Cadena con el nombre del dia de la semana
     */
    public String getDisplayNameOfDayOfWeek(DayOfWeek day) {
        return day.getDisplayName(TextStyle.FULL, colombianLocale);
    }

    /**
     * Actualiza el mensaje de resumen para la duracion de la reserva
     */
    public void updateSummaryMessage() {
        ensureAtLeastOneDaySelected();
        summaryMessage = generateWeeklyMessage();
        PrimeFaces.current().ajax().update("form:selectDaysButton");
    }

    /**
     * Asegura que al menos un dia este seleccionado
     */
    private void ensureAtLeastOneDaySelected() {
        if (selectedDays.isEmpty()) {
            selectedDays.add(LocalDate.now().getDayOfWeek());
        }
    }

    /**
     * Genera el mensaje de resumen para la duracion de la reserva
     *
     * @return Mensaje de resumen para la duracion de la reserva
     */
    private String generateWeeklyMessage() {
        String message = "se repite cada " + repetitions + " semana" + (repetitions > 1 ? "s " : " ");
        StringBuilder daysOfWeekBuilder = new StringBuilder();
        message += selectedDays.size() > 1 ? "los " : "el ";
        for (DayOfWeek day : selectedDays) {
            String dayShort = getDisplayNameOfDayOfWeek(day);
            if (selectedDays.size() > 1) {
                dayShort = day.getDisplayName(TextStyle.SHORT, colombianLocale);
            }
            if (!daysOfWeekBuilder.isEmpty()) {
                daysOfWeekBuilder.append(", ");
            }
            daysOfWeekBuilder.append(dayShort);
        }
        daysOfWeekBuilder.append(".");
        message += daysOfWeekBuilder.toString();
        return message;
    }

}
