package fr.miage.acm.wateringservice.farmer;

import fr.miage.acm.wateringservice.device.actuator.ActuatorService;
import fr.miage.acm.wateringservice.field.Field;
import fr.miage.acm.wateringservice.field.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FarmerService {

    private FarmerRepository farmerRepository;

    private FieldService fieldService;
    private ActuatorService actuatorService;

    public FarmerService(FarmerRepository farmerRepository, FieldService fieldService, ActuatorService actuatorService) {
        this.farmerRepository = farmerRepository;
        this.fieldService = fieldService;
        this.actuatorService = actuatorService;
    }

    public List<Farmer> findAll() {
        return farmerRepository.findAll();
    }

    public Optional<Farmer> findById(UUID id) {
        return farmerRepository.findById(id);
    }

    public Farmer findByEmail(String email) {
        return farmerRepository.findByEmail(email);
    }

    public Farmer save(Farmer farmer) {
        return farmerRepository.save(farmer);
    }

}
