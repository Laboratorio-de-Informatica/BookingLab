package edu.eci.labinfo.bookinglab.service;

import java.util.List;

import org.springframework.stereotype.Service;

import edu.eci.labinfo.bookinglab.data.RolesRepository;
import edu.eci.labinfo.bookinglab.model.RoleEntity;

/**
 * Clase que define los servicios de los roles
 * @version 1.0
 * @author Daniel Antonio Santanilla
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
