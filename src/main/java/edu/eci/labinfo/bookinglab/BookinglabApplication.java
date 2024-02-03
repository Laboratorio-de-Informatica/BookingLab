package edu.eci.labinfo.bookinglab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import edu.eci.labinfo.bookinglab.model.Role;
import edu.eci.labinfo.bookinglab.model.User;
import edu.eci.labinfo.bookinglab.service.UserService;

@SpringBootApplication
public class BookinglabApplication {

    @Autowired
    UserService myUserService;

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

        };
    }

}
