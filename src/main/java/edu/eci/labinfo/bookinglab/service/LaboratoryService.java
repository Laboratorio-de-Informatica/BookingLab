package edu.eci.labinfo.bookinglab.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.eci.labinfo.bookinglab.data.LaboratoryRepository;
import edu.eci.labinfo.bookinglab.model.BookingLabException;
import edu.eci.labinfo.bookinglab.model.Laboratory;

@Service
public class LaboratoryService {

    private final LaboratoryRepository laboratoryRepository;

    @Autowired
    public LaboratoryService(LaboratoryRepository laboratoryRepository) {
        this.laboratoryRepository = laboratoryRepository;
    }

    public Laboratory createLaboratory(Laboratory laboratory) throws BookingLabException {
        if (!laboratoryRepository.findByLaboratoryName(laboratory.getLaboratoryName().toUpperCase()).isEmpty()) {
            throw new BookingLabException(BookingLabException.LABORATORY_ALREADY_CREATED);
        }
        laboratory = nameFormatter(laboratory);
        return laboratoryRepository.save(laboratory);
    }

    public List<Laboratory> getAllLaboratories() {
        return laboratoryRepository.findAll();
    }

    private Laboratory nameFormatter(Laboratory laboratory) {
        return new Laboratory(laboratory.getLaboratoryName().toUpperCase(), laboratory.getAvailableComputers());
    }

    public Laboratory updateLaboratory(String labName, Laboratory updatedLab) throws BookingLabException {
        return laboratoryRepository.save(nameFormatter(laboratoryRepository.findByLaboratoryName(labName.toUpperCase())
                .orElseThrow(() -> new BookingLabException(BookingLabException.LABORATORY_NOT_FOUND))));
    }

    public void deleteLaboratory(String labName) throws BookingLabException {
        if (!laboratoryRepository.findByLaboratoryName(labName.toUpperCase()).isPresent()) {
            throw new BookingLabException(BookingLabException.LABORATORY_NOT_FOUND);
        }
        laboratoryRepository.deleteByLaboratoryName(labName.toUpperCase());
    }

    public void deleteAllLaboratories(){
        laboratoryRepository.deleteAll();
    }

    public Optional<Laboratory> getLaboratoryByName(String labname) throws BookingLabException{
        if(laboratoryRepository.findByLaboratoryName(labname.toUpperCase()).isEmpty()){
            throw  new BookingLabException(BookingLabException.LABORATORY_NOT_FOUND);
        }
        return laboratoryRepository.findByLaboratoryName(labname.toUpperCase());
    }

}
