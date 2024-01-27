package edu.eci.labinfo.bookinglab.model;

public class BookingLabException extends Exception {


    public static final String INCOMPLETE_FIELDS = "Por favor complete todos los campos.";
    public static final String CREDENTIALS_INCORRECT = "Su cuenta o contraseña es incorrecta.";
    public static final String UNVERIFIED_ACCOUNT = "Su cuenta no ha sido verificada.";

    public BookingLabException(String message) {
        super(message);
    }
}
