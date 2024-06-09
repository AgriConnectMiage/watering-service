package fr.miage.acm.wateringservice.device.measurement;


import fr.miage.acm.wateringservice.device.actuator.watering.scheduler.WateringScheduler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MeasurementService {

    private MeasurementRepository measurementRepository;

    public MeasurementService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    public Measurement createWateringMeasurement(WateringScheduler wateringScheduler) {
        Measurement measurement = new Measurement();
        measurement.setDateTime(LocalDateTime.now());
        measurement.setFarmerEmail(wateringScheduler.getActuator().getFarmer().getEmail());
        measurement.setFieldCoord(wateringScheduler.getActuator().getField().getCoord());
        measurement.setDuration(wateringScheduler.getDuration());
        measurementRepository.save(measurement);
        return measurement;
    }

}
