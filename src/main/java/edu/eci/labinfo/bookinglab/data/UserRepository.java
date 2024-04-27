package edu.eci.labinfo.bookinglab.data;

import edu.eci.labinfo.bookinglab.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interfaz que define las operaciones de la base de datos para los usuarios
 *
 * @author David Eduardo Valencia
 * @author Daniel Antonio Santanilla
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserName(String userName);
    Optional<UserEntity> findByFullName(String fullName);

}