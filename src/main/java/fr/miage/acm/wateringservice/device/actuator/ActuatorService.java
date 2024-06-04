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

    public List<Actuator> findAll() {
        return actuatorRepository.findAll();
    }

    public Actuator save(Actuator actuator) {
        return actuatorRepository.save(actuator);
    }

    public void delete(Actuator actuator) {
        actuatorRepository.delete(actuator);
    }

    public Optional<Actuator> findById(UUID id) {
        return actuatorRepository.findById(id);
    }

    public List<Actuator> findByFarmer(Farmer farmer) {
        return actuatorRepository.findByFarmer(farmer);
    }

    public Actuator addActuator(Farmer farmer) {
        Actuator actuator = new Actuator(farmer);
        return actuatorRepository.save(actuator);
    }

    @Transactional
    public void removeActuatorsByFarmer(Farmer farmer) {
        actuatorRepository.deleteByFarmer(farmer);
    }

    public Actuator assignActuatorToField(Actuator actuator, Field field) {
        actuator.setField(field);
        actuator.setState(DeviceState.OFF);
        return actuatorRepository.save(actuator);
    }

    public Actuator unassignActuatorFromField(Actuator actuator) {
        actuator.setField(null);
        actuator.setState(DeviceState.NOT_ASSIGNED);
        return actuatorRepository.save(actuator);
    }

    public Actuator changeState(Actuator actuator, DeviceState state) {
        actuator.setState(state);
        return actuatorRepository.save(actuator);

    }
}