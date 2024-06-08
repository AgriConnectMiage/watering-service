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
        wateringSchedulerRepository.save(wateringScheduler);
    }
}