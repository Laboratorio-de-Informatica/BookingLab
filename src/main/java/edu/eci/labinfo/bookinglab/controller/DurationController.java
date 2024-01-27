package edu.eci.labinfo.bookinglab.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import lombok.Data;

@Component
@RequestScoped
@Data
public class DurationController {

    private boolean mostrarSeccion = false;

    Logger logger;

    @PostConstruct
    public void init() {
        logger = LoggerFactory.getLogger(DurationController.class);
    }

    public void mostrarOcultarSeccion() {
        mostrarSeccion = !mostrarSeccion;
        logger.info("Seccion mostrada/ocultada");
    }
    
}
