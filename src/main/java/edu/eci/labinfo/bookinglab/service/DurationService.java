package edu.eci.labinfo.bookinglab.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.eci.labinfo.bookinglab.data.DurationRepository;
import edu.eci.labinfo.bookinglab.model.Duration;

@Service
public class DurationService {
    
    @Autowired
    DurationRepository durationRepository;

    public void saveDuration(Duration duration) {
        durationRepository.save(duration);
    }

    public Duration getDurationById(Long id) {
        return durationRepository.findById(id).get();
    }

    public void deleteDurationById(Long id) {
        durationRepository.deleteById(id);
    }

    public void updateDuration(Duration duration) {
        durationRepository.save(duration);
    }

}
