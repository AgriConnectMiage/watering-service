package fr.miage.acm.wateringservice.watering.log;

import fr.miage.acm.wateringservice.api.ApiActuator;
import fr.miage.acm.wateringservice.api.ApiWateringScheduler;
import fr.miage.acm.wateringservice.device.actuator.watering.scheduler.WateringScheduler;
import fr.miage.acm.wateringservice.device.measurement.MeasurementClient;
import fr.miage.acm.wateringservice.farmer.Farmer;
import fr.miage.acm.wateringservice.field.Field;
import org.springframework.stereotype.Service;

@Service
public class WateringLogService {
    private final WateringLogRepository wateringLogRepository;

    public WateringLogService(WateringLogRepository wateringLogRepository, MeasurementClient measurementClient) {
        this.wateringLogRepository = wateringLogRepository;
    }

    public void logWateringStart(Farmer farmer, Field field, float durationInSeconds) {
        // Convert duration from seconds to minutes and round up to the nearest integer
        int durationInMinutes = (int) Math.ceil(durationInSeconds / 60.0);

        // Concatenate field coordinates
        String fieldCoords = String.format("(%d, %d)", field.getXcoord(), field.getYcoord());

        // Concatenate farmer's first name and last name
        String farmerName = String.format("%s %s", farmer.getFirstName(), farmer.getLastName());

        // Create log message for the start of watering
        String message = String.format("Farmer %s started watering field %s for %d minutes", farmerName, fieldCoords, durationInMinutes);

        // Create and save the watering log
        WateringLog wateringLog = new WateringLog(message);
        wateringLogRepository.save(wateringLog);
    }


    public void logWateringEnd(Farmer farmer, Field field, WateringScheduler wateringScheduler) {
        // Concatenate field coordinates
        String fieldCoords = String.format("(%d, %d)", field.getXcoord(), field.getYcoord());
        // Concatenate farmer's first name and last name
        String farmerName = String.format("%s %s", farmer.getFirstName(), farmer.getLastName());
        // Create log message for the end of watering
        String message = String.format("Farmer %s finished watering field %s", farmerName, fieldCoords);

        // Create and save the watering log
        WateringLog wateringLog = new WateringLog(message);
        wateringLogRepository.save(wateringLog);
    }

    // Log watering cancellation
    public void logWateringCancellation(Farmer farmer, Field field, WateringScheduler wateringScheduler) {
        // Concatenate field coordinates
        String fieldCoords = String.format("(%d, %d)", field.getXcoord(), field.getYcoord());
        // Concatenate farmer's first name and last name
        String farmerName = String.format("%s %s", farmer.getFirstName(), farmer.getLastName());
        // Create log message for the cancellation of watering
        String message = String.format("Farmer %s cancelled watering field %s", farmerName, fieldCoords);


        // Create and save the watering log
        WateringLog wateringLog = new WateringLog(message);
        wateringLogRepository.save(wateringLog);
    }
}
