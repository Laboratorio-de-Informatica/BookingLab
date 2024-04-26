package edu.eci.labinfo.bookinglab.service;

import edu.eci.labinfo.bookinglab.data.UserRepository;
import edu.eci.labinfo.bookinglab.model.UserEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Clase que define los servicios de la entidad UserEntity
 *
 * @author Andres Camilo Oniate
 * @author Daniel Antonio Santanilla
 * @version 1.0
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Agrega un usuario a la base de datos
     *
     * @param user Usuario a agregar
     * @return Usuario agregado
     */
    public UserEntity addUser(UserEntity user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    /**
     * Obtiene un usuario por su nombre de usuario
     *
     * @param username Nombre de usuario
     * @return Usuario encontrado de lo contrario null
     */
    public UserEntity getUserByUserName(String username) {
        Optional<UserEntity> optinalUser = userRepository.findByUserName(username);
        if (optinalUser.isPresent()) {
            return optinalUser.get();
        }
        return null;
    }

    /**
     * Obtiene un usuario por su nombre completo
     *
     * @param fullName Nombre completo del usuario
     * @return Usuario encontrado de lo contrario null
     */
    public UserEntity getUserByFullName(String fullName) {
        Optional<UserEntity> optinalUser = userRepository.findByFullName(fullName);
        if (optinalUser.isPresent()) {
            return optinalUser.get();
        }
        return null;
    }

    /**
     * Obtiene todos los usuarios de la base de datos
     *
     * @return Lista de usuarios
     */
    public List<UserEntity> getUsers() {
        return userRepository.findAll();
    }

    /**
     * Actualiza un usuario en la base de datos
     *
     * @param user Usuario a actualizar
     * @return Usuario actualizado
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity updateUser(UserEntity user) {
        if (userRepository.existsById(user.getUserId())) {
            return userRepository.save(user);
        }
        return null;
    }

    /**
     * Elimina un usuario de la base de datos
     *
     * @param userName Nombre de usuario
     */
    public void deleteUser(String userName) {
        userRepository.delete(getUserByUserName(userName));
    }

    /**
     * Elimina todos los usuarios de la base de datos
     */
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

}