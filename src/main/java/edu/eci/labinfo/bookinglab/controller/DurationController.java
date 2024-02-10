package edu.eci.labinfo.bookinglab.controller;

import java.time.LocalDate;
import java.util.List;

import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import edu.eci.labinfo.bookinglab.service.BookingService;
import jakarta.annotation.PostConstruct;
import lombok.Data;

@Component
@SessionScope
@Data
public class DurationController {

    private int selectedOption;
    private String summaryMessage;
    private List<String> days;
    private List<String> selectedDays;
    private int repetitions;
    private int duration;
    Logger logger;

    @Autowired
    BookingService bookingService;

    @PostConstruct
    public void init() {
        selectedOption = 0;
        repetitions = 1;
        duration = 1;
        summaryMessage = "no se repite";
        days = bookingService.getWeekDays();
        logger = LoggerFactory.getLogger(DurationController.class);
    }

    public void startDuration() {
        selectedOption = 0;
        summaryMessage = "no se repite";
    }

    public void updateSummaryMessage() {
        ensureAtLeastOneDaySelected();
        switch (selectedOption) {
            case 1:
                summaryMessage = generateDailyMessage();
                break;
            case 2:
                summaryMessage = generateWeeklyMessage();
                break;
            default:
                summaryMessage = "no se repite";
                break;
        }
        PrimeFaces.current().ajax().update("form:selectDaysButton");
    }
    
    private void ensureAtLeastOneDaySelected() {
        if (selectedDays.isEmpty()) {
            selectedDays.add(days.get(LocalDate.now().getDayOfWeek().getValue() - 1));
        }
    }
    
    private String generateDailyMessage() {
        String message = "se repite cada " + repetitions + " dia" + (repetitions > 1 ? "s " : " ");
        return message;
    }
    
    private String generateWeeklyMessage() {
        String message = "se repite cada " + repetitions + " semana" + (repetitions > 1 ? "s " : " ");
        String daysOfWeek = "";
        message += selectedDays.size() > 1 ? "los " : "el ";
        for (String day : selectedDays) {
            String dayShort = day;
            if (selectedDays.size() > 1) {
                dayShort = day.substring(0, Math.min(day.length(), 3));
            }
            if (!daysOfWeek.isEmpty()) {
                daysOfWeek += ", ";
            }
            daysOfWeek += dayShort;
        }
        daysOfWeek += ".";
        message += daysOfWeek;
        return message;
    }

}
