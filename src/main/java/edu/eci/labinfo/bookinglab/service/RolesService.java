package edu.eci.labinfo.bookinglab.service;

import edu.eci.labinfo.bookinglab.data.RolesRepository;
import edu.eci.labinfo.bookinglab.model.RoleEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Clase que define los servicios de los roles
 *
 * @author Daniel Antonio Santanilla
 * @version 1.0
 */
@Service
public class RolesService {

    private final RolesRepository rolesRepository;

    public RolesService(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public List<RoleEntity> getAllRoles() {
        return rolesRepository.findAll();
    }

    public void deleteAll() {
        rolesRepository.deleteAll();
    }

}
