package edu.eci.labinfo.bookinglab.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Data;

@Component
@ViewScoped
@Data
public class DurationController {

    private String selectedOption;
    private String summaryMessage;
    private int repetition;
    private int duration;
    private List<String> selectedDays;
    private List<String> days;
    Logger logger;

    @PostConstruct
    public void init() {
        selectedOption = "0";
        summaryMessage = "no se repite";
        repetition = 1;
        duration = 1;
        days = List.of("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo");
        selectedDays = new ArrayList<>();
        logger = LoggerFactory.getLogger(DurationController.class);
    }

    public void updateSummaryMessage() {
        ensureAtLeastOneDaySelected();
        switch (selectedOption) {
            case "1":
                summaryMessage = generateDailyMessage();
                break;
            case "2":
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
        String message = "se repite cada " + repetition + " dia" + (repetition > 1 ? "s " : " ");
        return message;
    }
    
    private String generateWeeklyMessage() {
        String message = "se repite cada " + repetition + " semana" + (repetition > 1 ? "s " : " ");
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
