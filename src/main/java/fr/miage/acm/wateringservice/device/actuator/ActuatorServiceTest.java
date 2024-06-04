package fr.miage.acm.wateringservice.device.actuator;

import org.springframework.stereotype.Component;

@Component
public class ActuatorServiceTest {
    private final ActuatorService actuatorService;

    public ActuatorServiceTest(ActuatorService actuatorService) {
        this.actuatorService = actuatorService;
    }

}
