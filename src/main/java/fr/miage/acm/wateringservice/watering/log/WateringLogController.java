package fr.miage.acm.wateringservice.watering.log;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/watering-logs")
public class WateringLogController {
    private final WateringLogService wateringLogService;

    public WateringLogController(WateringLogService wateringLogService) {
        this.wateringLogService = wateringLogService;
    }
}
