package edu.eci.labinfo.bookinglab.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public enum Day {

    LUNES("Lunes"),
    MARTES("Martes"),
    MIERCOLES("Miercoles"),
    JUEVES("Jueves"),
    VIERNES("Viernes"),
    SABADO("Sabado"),
    DOMINGO("Domingo");

    private String value;

    Day(String value) {
        this.value = value;
    }

    public static Day findByValue(String day) {
        Day response = null;
        for (Day d : Day.values()) {
            if (d.getValue().equalsIgnoreCase(day)) {
                response = d;
                break;
            }
        }
        return response;
    }

    public static List<String> getAllDays() {
        List<String> days = new ArrayList<>();
        for (Day d : Day.values()) {
            days.add(d.getValue());
        }
        return days;
    }

}
