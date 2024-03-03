package edu.eci.labinfo.bookinglab.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.eci.labinfo.bookinglab.model.UserEntity;

/**
 * Interfaz que define las operaciones de la base de datos para los usuarios
 * @version 1.0
 * @author David Eduardo Valencia
 * @author Daniel Antonio Santanilla
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserName(String userName);

    Optional<UserEntity> findByFullName(String fullName);
    
}