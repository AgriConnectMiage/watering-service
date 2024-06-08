package fr.miage.acm.wateringservice.device.actuator.watering.scheduler;

import fr.miage.acm.wateringservice.device.actuator.Actuator;
import fr.miage.acm.wateringservice.device.actuator.ActuatorService;
import org.springframework.stereotype.Service;

@Service
public class WateringSchedulerService {

    private ActuatorService actuatorService;
    private WateringSchedulerRepository wateringSchedulerRepository;

    public WateringSchedulerService(ActuatorService actuatorService, WateringSchedulerRepository wateringSchedulerRepository) {
        this.actuatorService = actuatorService;
        this.wateringSchedulerRepository = wateringSchedulerRepository;
    }

    public void addWateringSchedulerToActuator(WateringScheduler wateringScheduler, Actuator actuator) {
        // Check if actuator is assigned to a field
        if (actuator.getField() == null) {
            throw new IllegalArgumentException("Actuator is not assigned to a field");
        }
        wateringScheduler.setActuator(actuator);
        wateringSchedulerRepository.save(wateringScheduler);
    }
}