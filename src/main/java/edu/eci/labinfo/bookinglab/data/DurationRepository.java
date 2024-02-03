package edu.eci.labinfo.bookinglab.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.eci.labinfo.bookinglab.model.Duration;

@Repository
public interface DurationRepository extends JpaRepository<Duration, Long> {

}
