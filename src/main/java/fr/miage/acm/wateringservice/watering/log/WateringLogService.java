package fr.miage.acm.wateringservice.watering.log;

import org.springframework.stereotype.Service;

@Service
public class WateringLogService {
    private final WateringLogRepository wateringLogRepository;

    public WateringLogService(WateringLogRepository wateringLogRepository) {
        this.wateringLogRepository = wateringLogRepository;
    }

    // Create message from farmer, field and watering duration

}
