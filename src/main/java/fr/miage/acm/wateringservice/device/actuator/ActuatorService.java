package fr.miage.acm.wateringservice.device.actuator;

import fr.miage.acm.wateringservice.device.DeviceState;
import fr.miage.acm.wateringservice.farmer.Farmer;
import fr.miage.acm.wateringservice.field.Field;
import fr.miage.acm.wateringservice.field.FieldRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ActuatorService {

    private ActuatorRepository actuatorRepository;

    private FieldRepository fieldRepository;

    public ActuatorService(ActuatorRepository actuatorRepository, FieldRepository fieldRepository) {
        this.actuatorRepository = actuatorRepository;
        this.fieldRepository = fieldRepository;
    }

    public List<Actuator> findAll() {
        return actuatorRepository.findAll();
    }

    public Actuator save(Actuator actuator) {
        return actuatorRepository.save(actuator);
    }

    public Optional<Actuator> findById(UUID id) {
        return actuatorRepository.findById(id);
    }

    public List<Actuator> findByFarmer(Farmer farmer) {
        return actuatorRepository.findByFarmer(farmer);
    }

    public Actuator changeState(Actuator actuator, DeviceState state) {
        actuator.setState(state);
        return actuatorRepository.save(actuator);

    }
}