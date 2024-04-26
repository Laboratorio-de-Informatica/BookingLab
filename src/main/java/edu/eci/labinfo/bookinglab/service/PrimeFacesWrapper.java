package edu.eci.labinfo.bookinglab.service;

import org.primefaces.PrimeFaces;
import org.primefaces.context.PrimeRequestContext;
import org.springframework.stereotype.Service;

/**
 * Clase que envuelve las funcionalidades de PrimeFaces
 *
 * @version 1.0
 * @autor Daniel Antonio Santanilla
 */
@Service
public class PrimeFacesWrapper {

    public PrimeFaces current() {
        return PrimeFaces.current();
    }

    public PrimeRequestContext getRequestContext() {
        return PrimeRequestContext.getCurrentInstance();
    }

}
