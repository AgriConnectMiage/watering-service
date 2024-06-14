package fr.miage.acm.wateringservice.watering.log;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/watering-logs")
public class WateringLogController {
    private final WateringLogService wateringLogService;

    public WateringLogController(WateringLogService wateringLogService) {
        this.wateringLogService = wateringLogService;
    }

    @GetMapping
    public List<WateringLog> findAll() {
        return wateringLogService.findAll();
    }

    // find by farmer id
    @GetMapping("/farmer/{farmerId}")
    public List<WateringLog> findByFarmer(@PathVariable UUID farmerId) {
        return wateringLogService.findByFarmerId(farmerId);
    }


}
