package edu.eci.labinfo.bookinglab.service;

import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.eci.labinfo.bookinglab.data.LaboratoryRepository;
import edu.eci.labinfo.bookinglab.model.BookingLabException;
import edu.eci.labinfo.bookinglab.model.Laboratory;

@Service
public class LaboratoryService {
    
    @Autowired
    private LaboratoryRepository laboratoryRepository;

    public Laboratory createLaboratory(Laboratory laboratory)throws BookingLabException{
        if(!laboratoryRepository.findByLaboratoryName(laboratory.getLaboratoryName().toUpperCase()).isEmpty()){
            throw new BookingLabException(BookingLabException.LABORATORY_ALREADY_CREATED);
        }
        laboratory = nameFormatter(laboratory);
        return laboratoryRepository.save(laboratory);
    }

    public List<Laboratory> getAllLaboratories(){
        return laboratoryRepository.findAll();
    }



    private Laboratory nameFormatter(Laboratory laboratory) {
        return new Laboratory(laboratory.getLaboratoryName().toUpperCase(), laboratory.getAvailableComputers());
    }



}
