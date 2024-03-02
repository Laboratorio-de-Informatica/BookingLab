package edu.eci.labinfo.bookinglab.model;

import lombok.Getter;

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
