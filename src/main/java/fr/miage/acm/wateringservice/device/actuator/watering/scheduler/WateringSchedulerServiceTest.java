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

    public void addManualWateringSchedulerToActuator() {
        Actuator actuator = actuatorService.findAll().get(0);
        LocalDateTime beginDate = LocalDateTime.now();
        float duration = 1000;
        WateringScheduler wateringScheduler = new WateringScheduler(beginDate, duration);
        wateringSchedulerService.addManualWateringSchedulerToActuator(wateringScheduler, actuator);
    }

    public void deleteWateringScheduler() {
        Actuator actuator = actuatorService.findAll().get(0);
        WateringScheduler wateringscheduler = wateringSchedulerService.findByActuator(actuator);
        wateringSchedulerService.deleteWateringScheduler(wateringscheduler);
    }

}
