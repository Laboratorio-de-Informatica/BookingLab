package edu.eci.labinfo.bookinglab.utils;

import java.util.HashMap;
import java.util.Map;

import edu.eci.labinfo.bookinglab.model.Laboratory;

public class LabColorManager {

    private Map<Laboratory, String> labColors;
    private static LabColorManager instance;

    public LabColorManager() {
        this.labColors = new HashMap<>();
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

    public String getColor(Laboratory lab) {
        return labColors.get(lab);
    }
    
}
