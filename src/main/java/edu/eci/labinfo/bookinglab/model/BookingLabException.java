package edu.eci.labinfo.bookinglab.model;

public class BookingLabException extends Exception {

    public static final String INCOMPLETE_FIELDS = "Por favor complete todos los campos.";
    public static final String CREDENTIALS_INCORRECT = "Su cuenta o contraseña es incorrecta.";
    public static final String UNVERIFIED_ACCOUNT = "Su cuenta no ha sido verificada.";
    public static final String INITDATE_MAYOR_ENDDATE = "La Hora inicial no puede ser mayor a la final";
    public static final String DATE_ALREADY_TAKEN = "Esta fecha se encuentra ocupada";
    public static final String RESERVATION_NOT_FOUND = "No se encuentra una reserva con esas caracteristicas";
    public static final String LABORATORY_ALREADY_CREATED = "Este laboratorio ya existe";
    public static final String LABORATORY_NOT_FOUND = "El laboratorio no se encuentra";

    public BookingLabException(String message) {
        super(message);
    }
}
