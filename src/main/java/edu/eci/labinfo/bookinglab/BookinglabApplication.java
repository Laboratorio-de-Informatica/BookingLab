package edu.eci.labinfo.bookinglab;

import edu.eci.labinfo.bookinglab.model.RoleEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import edu.eci.labinfo.bookinglab.model.Role;
import edu.eci.labinfo.bookinglab.model.UserEntity;
import edu.eci.labinfo.bookinglab.service.RolesService;
import edu.eci.labinfo.bookinglab.service.UserService;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class BookinglabApplication {

    private final UserService myUserService;
    private final RolesService myRolesService;

    public BookinglabApplication(UserService myUserService , RolesService myRolesService) {
        this.myUserService = myUserService;
        this.myRolesService = myRolesService;
    }

    public static void main(String[] args) {
        SpringApplication.run(BookinglabApplication.class, args);

    }

    @Bean(name = "database")
    public CommandLineRunner run() {
        return args -> {

            myUserService.deleteAllUsers();
            myRolesService.deleteAll();

            Set<RoleEntity> roles = new HashSet<>();
            roles.add(RoleEntity.builder().name(Role.ADMINISTRADOR).build());

            UserEntity user = new UserEntity();
            user.setFullName("Laboratorio de Informática");
            user.setUserName("labinfo");
            user.setPassword("1234567890");
            user.setRoles(roles);
            myUserService.addUser(user);

        };
    }

}
