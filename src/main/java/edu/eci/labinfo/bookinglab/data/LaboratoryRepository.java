package edu.eci.labinfo.bookinglab.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.eci.labinfo.bookinglab.model.Laboratory;

@Repository
public interface LaboratoryRepository extends JpaRepository<Laboratory, Long> {

    Optional<Laboratory> findByLaboratoryName(String laboratoryName);

    void deleteByLaboratoryName(String laboratoryName);
    
}
