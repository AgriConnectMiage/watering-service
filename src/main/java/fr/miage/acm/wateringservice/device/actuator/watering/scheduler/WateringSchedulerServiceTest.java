package fr.miage.acm.wateringservice.device.actuator.watering.scheduler;

import fr.miage.acm.wateringservice.device.actuator.Actuator;
import fr.miage.acm.wateringservice.device.actuator.ActuatorService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class WateringSchedulerServiceTest {
    private final ActuatorService actuatorService;
    private final WateringSchedulerService wateringSchedulerService;

    public WateringSchedulerServiceTest(ActuatorService actuatorService, WateringSchedulerService wateringSchedulerService) {
        this.actuatorService = actuatorService;
        this.wateringSchedulerService = wateringSchedulerService;
    }

    public void addWateringSchedulerToActuator() {
        Actuator actuator = actuatorService.findAll().get(0);
        LocalDateTime beginDate = LocalDateTime.now();
        float duration = 600;
        WateringScheduler wateringScheduler = new WateringScheduler(beginDate, duration);
        wateringSchedulerService.addWateringSchedulerToActuator(wateringScheduler, actuator);
    }

}
