package edu.eci.labinfo.bookinglab.data;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.eci.labinfo.bookinglab.model.RoleEntity;

public interface RolesRepository  extends JpaRepository<RoleEntity, Long>  {
    
}
