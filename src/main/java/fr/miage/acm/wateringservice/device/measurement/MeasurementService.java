package fr.miage.acm.wateringservice.device.measurement;


import fr.miage.acm.wateringservice.api.ApiActuator;
import fr.miage.acm.wateringservice.api.ApiWateringScheduler;
import fr.miage.acm.wateringservice.client.MeasurementServiceClient;
import fr.miage.acm.wateringservice.device.actuator.watering.scheduler.WateringScheduler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final MeasurementServiceClient measurementServiceClient;

    public MeasurementService(MeasurementRepository measurementRepository, MeasurementServiceClient measurementServiceClient) {
        this.measurementRepository = measurementRepository;
        this.measurementServiceClient = measurementServiceClient;
    }

    public Measurement createWateringMeasurement(WateringScheduler wateringScheduler) {
        return measurementServiceClient.createWateringMeasurement(new ApiWateringScheduler(LocalDateTime.now(),
                wateringScheduler.getDuration(), wateringScheduler.getHumidityThreshold(),
                new ApiActuator(wateringScheduler.getActuator())));
    }
}
