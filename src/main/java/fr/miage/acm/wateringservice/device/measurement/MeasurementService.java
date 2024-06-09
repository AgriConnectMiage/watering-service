package fr.miage.acm.wateringservice.device.measurement;


import fr.miage.acm.wateringservice.api.ApiActuator;
import fr.miage.acm.wateringservice.api.ApiFarmer;
import fr.miage.acm.wateringservice.api.ApiWateringScheduler;
import fr.miage.acm.wateringservice.device.actuator.watering.scheduler.WateringScheduler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final MeasurementClient measurementClient;

    public MeasurementService(MeasurementRepository measurementRepository, MeasurementClient measurementClient) {
        this.measurementRepository = measurementRepository;
        this.measurementClient = measurementClient;
    }

    public Measurement createWateringMeasurement(WateringScheduler wateringScheduler) {
        System.out.println("appel");
        return measurementClient.createWateringMeasurement(new ApiWateringScheduler(LocalDateTime.now(),
                wateringScheduler.getDuration(), wateringScheduler.getHumidityThreshold(),
                new ApiActuator(wateringScheduler.getActuator())));
    }

}
