package edu.eci.labinfo.bookinglab.utils;

import java.util.HashMap;
import java.util.Map;

import edu.eci.labinfo.bookinglab.model.Laboratory;

public class LabColorManager {

    private Map<Laboratory, String> labColors;
    private static LabColorManager instance;

    public LabColorManager() {
        this.labColors = new HashMap<>();
        labColors.put(Laboratory.MULTIMEDIA, "#9CE3A9");
        labColors.put(Laboratory.SOFTWARE, "#F5F983");
        labColors.put(Laboratory.REDES, "#FE9554");
        labColors.put(Laboratory.PLATAFORMAS, "#C785AD");
        labColors.put(Laboratory.INTERACTIVA, "#9CC1E3");
        labColors.put(Laboratory.FUNDAMENTOS, "#E3F6CE");
        labColors.put(Laboratory.VIDEOJUEGOS, "#E2A9F3");
        labColors.put(Laboratory.AULA_EDFI, "#DBB8A0");
    }

    public static LabColorManager getInstance() {
        if (instance == null) {
            instance = new LabColorManager();
        }
        return instance;
    }

    public String getColor(Laboratory lab) {
        return labColors.get(lab);
    }
    
}
