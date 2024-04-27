package edu.eci.labinfo.bookinglab.data;

import edu.eci.labinfo.bookinglab.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interfaz que define las operaciones de la base de datos para los roles
 *
 * @version 1.0
 * @autor Daniel Antonio Santanilla
 */
public interface RolesRepository extends JpaRepository<RoleEntity, Long> {

}
