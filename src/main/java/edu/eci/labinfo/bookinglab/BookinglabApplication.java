package edu.eci.labinfo.bookinglab;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import edu.eci.labinfo.bookinglab.model.Laboratory;
import edu.eci.labinfo.bookinglab.model.Role;
import edu.eci.labinfo.bookinglab.model.User;
import edu.eci.labinfo.bookinglab.service.LaboratoryService;
import edu.eci.labinfo.bookinglab.service.ReservationService;
import edu.eci.labinfo.bookinglab.service.UserService;

@SpringBootApplication
public class BookinglabApplication {

	@Autowired
	UserService myUserService;

	@Autowired
	LaboratoryService laboratoryService;

	public static void main(String[] args) {
		SpringApplication.run(BookinglabApplication.class, args);

	}

	@Bean(name = "database")
	public CommandLineRunner run() {
	return args -> {
		
		myUserService.deleteAllUsers();
		
		User user = new User();
		user.setFullName("Laboratorío de Informática");
		user.setUserName("labinfo");
		user.setPassword("1234567890");
		user.setRole(Role.ADMINISTRADOR);
		myUserService.addUser(user);
		HashMap<String, Laboratory> laboratorios = new HashMap<>();
        String[] nombres = {"Plataformas", "Redes", "Software", "Interactiva", "Fundamentos", "Videojuegos"};
        int[] capacidades = {24, 16, 24, 7, 24, 22};
        for (int i = 0; i < nombres.length; ++i) {
            Laboratory lab = new Laboratory(nombres[i], capacidades[i]);
            laboratorios.put(nombres[i], lab);
        }
		for(int i = 0; i < nombres.length;++i){
			laboratoryService.createLaboratory(laboratorios.get(nombres[i]));
		}
	
		};
	}

}
