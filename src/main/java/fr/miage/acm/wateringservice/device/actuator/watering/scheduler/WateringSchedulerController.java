package fr.miage.acm.wateringservice.device.actuator.watering.scheduler;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/actuators/{actuatorId}/watering-schedulers")
public class WateringSchedulerController {

    @GetMapping("/{schedulerId}")
    public String getWateringScheduler(@PathVariable Long actuatorId, @PathVariable Long schedulerId) {
        return "Watering Scheduler " + schedulerId + " for Actuator " + actuatorId;
    }

}
