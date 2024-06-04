package fr.miage.acm.wateringservice.device.actuator;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ActuatorController {
        @RequestMapping("/actuators")
    public String getActuators() {
        return "All actuators";
    }
}
