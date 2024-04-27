package edu.eci.labinfo.bookinglab.model;

import lombok.Getter;

/**
 * Enumeración que representa los roles de los usuarios
 * @version 1.0
 * @author David Eduardo Valencia
 * @author Daniel Antonio Santanilla
 * @author Andres Camilo Oniate
 */
@Getter
public enum Role {

    ADMINISTRADOR("Administrador"),
    PROFESOR("Profesor");

    private String value;

    Role(String value) {
        this.value = value;
    }
    
    public static Role findByValue(String role) {
        Role response = null;
        for (Role r : Role.values()) {
            if (r.getValue().equalsIgnoreCase(role)) {
                response = r;
                break;
            }
        }
        return response;
    }

}
