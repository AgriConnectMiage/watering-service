package fr.miage.acm.wateringservice.device.actuator.watering.scheduler;

import fr.miage.acm.wateringservice.device.actuator.Actuator;
import fr.miage.acm.wateringservice.device.actuator.ActuatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/actuators/{actuatorId}/watering-schedulers")
public class WateringSchedulerController {

    private final WateringSchedulerService wateringSchedulerService;
    private final ActuatorService actuatorService;

    @Autowired
    public WateringSchedulerController(WateringSchedulerService wateringSchedulerService, ActuatorService actuatorService) {
        this.wateringSchedulerService = wateringSchedulerService;
        this.actuatorService = actuatorService;
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
    public String deleteWateringScheduler(@PathVariable UUID actuatorId, @PathVariable UUID schedulerId) {
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
}
