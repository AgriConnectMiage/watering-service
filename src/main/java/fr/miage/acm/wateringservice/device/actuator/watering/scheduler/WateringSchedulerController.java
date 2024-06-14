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

    @Autowired
    public WateringSchedulerController(WateringSchedulerService wateringSchedulerService, ActuatorService actuatorService) {
        this.wateringSchedulerService = wateringSchedulerService;
        this.actuatorService = actuatorService;
    }

    @GetMapping
    public WateringScheduler getAllWateringSchedulers(@PathVariable UUID actuatorId) {
        Optional<Actuator> optionalActuator = actuatorService.findById(actuatorId);
        if (!optionalActuator.isPresent()) {
            throw new RuntimeException("Actuator not found");
        }
        Actuator actuator = optionalActuator.get();

        return wateringSchedulerService.findByActuator(actuator);
    }

    @PostMapping
    public String addManualWateringScheduler(@PathVariable UUID actuatorId, @RequestBody WateringScheduler wateringScheduler) {
        Optional<Actuator> optionalActuator = actuatorService.findById(actuatorId);
        if (!optionalActuator.isPresent()) {
            return "Actuator not found";
        }
        Actuator actuator = optionalActuator.get();

        wateringSchedulerService.addManualWateringSchedulerToActuator(wateringScheduler, actuator);
        return "Watering Scheduler added for Actuator " + actuatorId;
    }

    @DeleteMapping("/{schedulerId}")
    public String deleteIntelligentWateringScheduler(@PathVariable UUID actuatorId, @PathVariable UUID schedulerId) {
        Optional<Actuator> optionalActuator = actuatorService.findById(actuatorId);
        if (!optionalActuator.isPresent()) {
            return "Actuator not found";
        }
        Actuator actuator = optionalActuator.get();

        Optional<WateringScheduler> optionalWateringScheduler = wateringSchedulerService.findById(schedulerId);
        if (!optionalWateringScheduler.isPresent() || !optionalWateringScheduler.get().getActuator().getId().equals(actuatorId)) {
            return "Watering Scheduler not found for Actuator " + actuatorId;
        }

        wateringSchedulerService.deleteWateringScheduler(optionalWateringScheduler.get());
        return "Watering Scheduler " + schedulerId + " deleted for Actuator " + actuatorId;
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
