package fr.miage.acm.wateringservice.device.actuator.watering.scheduler;

import fr.miage.acm.wateringservice.device.actuator.Actuator;
import fr.miage.acm.wateringservice.device.actuator.ActuatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/actuators/{actuatorId}/watering-scheduler")
public class WateringSchedulerController {

    private final WateringSchedulerService wateringSchedulerService;
    private final ActuatorService actuatorService;

    public WateringSchedulerController(WateringSchedulerService wateringSchedulerService, ActuatorService actuatorService) {
        this.wateringSchedulerService = wateringSchedulerService;
        this.actuatorService = actuatorService;
    }

    @PostMapping
    public String addManualWateringScheduler(@PathVariable UUID actuatorId, @RequestParam float duration) {
        Optional<Actuator> optionalActuator = actuatorService.findById(actuatorId);
        if (!optionalActuator.isPresent()) {
            return "Actuator not found";
        }
        Actuator actuator = optionalActuator.get();

        WateringScheduler wateringScheduler = new WateringScheduler(duration);

        wateringSchedulerService.addManualWateringSchedulerToActuator(wateringScheduler, actuator);
        return "Watering Scheduler added for Actuator " + actuatorId;
    }

    @PostMapping("/intelligent")
    public String addIntelligentWateringScheduler(@PathVariable UUID actuatorId, @RequestParam int threshold, @RequestParam float duration) {
        Optional<Actuator> optionalActuator = actuatorService.findById(actuatorId);
        if (!optionalActuator.isPresent()) {
            return "Actuator not found";
        }
        Actuator actuator = optionalActuator.get();

        WateringScheduler wateringScheduler = new WateringScheduler(threshold, duration);

        wateringSchedulerService.addIntelligentWateringSchedulerToActuator(wateringScheduler, actuator);
        return "Intelligent Watering Scheduler added for Actuator " + actuatorId;
    }

    @GetMapping("/{schedulerId}")
    public WateringScheduler getWateringScheduler(@PathVariable UUID actuatorId, @PathVariable UUID schedulerId) {
        Optional<Actuator> optionalActuator = actuatorService.findById(actuatorId);
        if (!optionalActuator.isPresent()) {
            throw new RuntimeException("Actuator not found");
        }
        Actuator actuator = optionalActuator.get();

        Optional<WateringScheduler> optionalWateringScheduler = wateringSchedulerService.findById(schedulerId);
        if (!optionalWateringScheduler.isPresent() || !optionalWateringScheduler.get().getActuator().getId().equals(actuatorId)) {
            throw new RuntimeException("Watering Scheduler not found for Actuator " + actuatorId);
        }

        return optionalWateringScheduler.get();
    }

    // trigger intelligent watering with watering scheduler id as parameter
    @PostMapping("/{schedulerId}/intelligent-watering")
    public String triggerIntelligentWatering(@PathVariable UUID actuatorId, @PathVariable UUID schedulerId) {
        Optional<Actuator> optionalActuator = actuatorService.findById(actuatorId);
        if (!optionalActuator.isPresent()) {
            return "Actuator not found";
        }
        Actuator actuator = optionalActuator.get();

        Optional<WateringScheduler> optionalWateringScheduler = wateringSchedulerService.findById(schedulerId);
        if (!optionalWateringScheduler.isPresent() || !optionalWateringScheduler.get().getActuator().getId().equals(actuatorId)) {
            return "Watering Scheduler not found for Actuator " + actuatorId;
        }

        wateringSchedulerService.triggerIntelligentWatering(optionalWateringScheduler.get());
        return "Intelligent watering triggered for Watering Scheduler " + schedulerId + " of Actuator " + actuatorId;
    }

    @PostMapping("/{schedulerId}/cancel")
    public String cancelWateringInProgress(@PathVariable UUID actuatorId, @PathVariable UUID schedulerId) {
        Optional<Actuator> optionalActuator = actuatorService.findById(actuatorId);
        if (!optionalActuator.isPresent()) {
            return "Actuator not found";
        }
        Actuator actuator = optionalActuator.get();

        Optional<WateringScheduler> optionalWateringScheduler = wateringSchedulerService.findById(schedulerId);
        if (!optionalWateringScheduler.isPresent() || !optionalWateringScheduler.get().getActuator().getId().equals(actuatorId)) {
            return "Watering Scheduler not found for Actuator " + actuatorId;
        }

        wateringSchedulerService.cancelWateringInProgress(actuator);
        return "Watering in progress canceled for Watering Scheduler " + schedulerId + " of Actuator " + actuatorId;
    }
}
