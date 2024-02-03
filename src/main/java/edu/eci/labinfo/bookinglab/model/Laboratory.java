package edu.eci.labinfo.bookinglab.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public enum Laboratory {

    SOFTWARE("Software"),
    REDES("Redes"),
    PLATAFORMAS("Plataformas"),
    MULTIMEDIA("Multimedia"),
    AULA_EDFI("Aula EDFI"),
    INTERACTIVA("Interactiva"),
    FUNDAMENTOS("Fundamentos"),
    VIDEOJUEGOS("Videojuegos");

    private String value;

    Laboratory(String value) {
        this.value = value;
    }

    public static Laboratory findByValue(String laboratory) {
        Laboratory response = null;
        for (Laboratory l : Laboratory.values()) {
            if (l.getValue().equalsIgnoreCase(laboratory)) {
                response = l;
                break;
            }
        }
        return response;
    }

    public static List<String> getAllLaboratories() {
        List<String> laboratories = new ArrayList<>();
        for (Laboratory l : Laboratory.values()) {
            laboratories.add(l.getValue());
        }
        return laboratories;
    }

}
