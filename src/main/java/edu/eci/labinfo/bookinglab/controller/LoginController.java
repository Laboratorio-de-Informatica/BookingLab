package edu.eci.labinfo.bookinglab.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import edu.eci.labinfo.bookinglab.model.BookingLabException;
import edu.eci.labinfo.bookinglab.model.User;
import edu.eci.labinfo.bookinglab.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import lombok.Data;

@Component
@Data
@SessionScope
public class LoginController {

    @Autowired
    UserService userService;
    private String userName;
    private String password;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private static final String LOGIN_FORM_MESSAGES = "form:messages";
    private static final String ERROR = "Error";


    @PostConstruct
    public void init() {
    }

    public List<User> getUsers() {
        return userService.getUsers();
    }

    public Boolean login() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // Verificar que se ingresó un nombre de usuario y una contraseña
        if (password == null || userName == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, BookingLabException.INCOMPLETE_FIELDS, ERROR));
            PrimeFaces.current().ajax().update(LOGIN_FORM_MESSAGES);
            return false;
        }
        // Buscar al usuario por nombre de usuario
        User userToLogin = userService.getUserByUserName(userName);
        // Si el usuario no existe o la contraseña es incorrecta, mostrar un mensaje de error y salir temprano
        if (userToLogin == null  || !passwordEncoder.matches(password, userToLogin.getPassword())) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, BookingLabException.CREDENTIALS_INCORRECT, ERROR));
            PrimeFaces.current().ajax().update(LOGIN_FORM_MESSAGES);
            return false;
        }
        // Si el usuario está autenticado, redirigirlo a la página correspondiente
        try {
            password = null;
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            String redirectPath = "index.xhtml";
            ec.redirect(ec.getRequestContextPath() + redirectPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Obtiene el nombre del usuario actual extraido de la base de datos.
     * 
     * @param userName el nombre del usuario a obtener.
     * @return el nombre del usuario actual.
     */
    public String getCurrentUserName(String userName) {
        return userService.getUserByUserName(userName).getUserName();
    }

    /**
     * Obtiene el nombre completo del usuario actual extraido de la base de datos.
     * 
     * @param userName el nombre del usuario a obtener.
     * @return el nombre completo del usuario actual.
     */
    public String getCurrentFullName(String userName) {
        return userService.getUserByUserName(userName).getFullName();
    }

     /**
     * Función que permite el cierre de sesión
     * 
     * @return True si el cierre de sesión es exitoso, de lo contrario False
     */
    public Boolean logout() {
        userName = null;
        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            String redirectPath = "../login.xhtml";
            ec.redirect(ec.getRequestContextPath() + redirectPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    
}