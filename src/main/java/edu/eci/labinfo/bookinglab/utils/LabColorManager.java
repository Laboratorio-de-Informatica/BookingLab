package edu.eci.labinfo.bookinglab.utils;

import edu.eci.labinfo.bookinglab.model.Laboratory;

import java.util.EnumMap;
import java.util.Map;

/**
 * Clase que maneja los colores de los laboratorios
 *
 * @author Daniel Antonio Santanilla
 * @version 1.0
 */
public class LabColorManager {

    private final Map<Laboratory, String> labColors;
    private static LabColorManager instance;

    public LabColorManager() {
        this.labColors = new EnumMap<>(Laboratory.class);
        labColors.put(Laboratory.MULTIMEDIA, "#FFC107");
        labColors.put(Laboratory.SOFTWARE, "#FAB710");
        labColors.put(Laboratory.REDES, "#FD7E14");
        labColors.put(Laboratory.PLATAFORMAS, "#6F42C1");
        labColors.put(Laboratory.INTERACTIVA, "#0DCAF0");
        labColors.put(Laboratory.FUNDAMENTOS, "#198754");
        labColors.put(Laboratory.VIDEOJUEGOS, "#D63384");
        labColors.put(Laboratory.AULA_EDFI, "#20C997");
    }

    public static LabColorManager getInstance() {
        if (instance == null) {
            instance = new LabColorManager();
        }
        return instance;
    }

    /**
     * Obtiene el color de un laboratorio
     *
     * @param lab Laboratorio
     * @return Color del laboratorio
     */
    public String getColor(Laboratory lab) {
        return labColors.get(lab);
    }

}
