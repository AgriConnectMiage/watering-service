package fr.miage.acm.wateringservice.device.actuator.watering.scheduler;

import fr.miage.acm.wateringservice.device.DeviceState;
import fr.miage.acm.wateringservice.device.actuator.Actuator;
import fr.miage.acm.wateringservice.device.actuator.ActuatorService;
import fr.miage.acm.wateringservice.device.measurement.MeasurementService;
import fr.miage.acm.wateringservice.watering.log.WateringLogService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class WateringSchedulerService {

    private final WateringSchedulerRepository wateringSchedulerRepository;
    private final ActuatorService actuatorService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final WateringLogService wateringLogService;
    private final MeasurementService measurementService;
    private final ConcurrentHashMap<UUID, WateringScheduler> schedulerMap = new ConcurrentHashMap<>();

    public WateringSchedulerService(WateringSchedulerRepository wateringSchedulerRepository, ActuatorService actuatorService, WateringLogService wateringLogService, MeasurementService measurementService) {
        this.wateringSchedulerRepository = wateringSchedulerRepository;
        this.actuatorService = actuatorService;
        this.wateringLogService = wateringLogService;
        this.measurementService = measurementService;
    }

    public boolean isWateringInProgress(WateringScheduler wateringScheduler){
        LocalDateTime now = LocalDateTime.now();
        return wateringScheduler.getBeginDate().isBefore(now) && wateringScheduler.getEndDate().isAfter(now);
    }

    @PostConstruct
    public void initialize() {
        List<WateringScheduler> allWateringSchedulers = wateringSchedulerRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (WateringScheduler wateringScheduler : allWateringSchedulers) {
            if (isWateringInProgress(wateringScheduler)) {
                Actuator actuator = wateringScheduler.getActuator();
                actuatorService.changeState(actuator, DeviceState.ON);
                schedulerMap.put(actuator.getId(), wateringScheduler);

                long delay = ChronoUnit.SECONDS.between(now, wateringScheduler.getEndDate());
                this.scheduler.schedule(() -> {
                    actuatorService.changeState(actuator, DeviceState.OFF);
                    measurementService.createWateringMeasurement(wateringScheduler);
                    wateringLogService.logWateringEnd(actuator.getField().getFarmer(), actuator.getField(), wateringScheduler);
                    schedulerMap.remove(actuator.getId());
                }, delay, TimeUnit.SECONDS);
            }
        }
    }

    public Optional<WateringScheduler> findById(UUID id) {
        return wateringSchedulerRepository.findById(id);
    }

    public void addManualWateringSchedulerToActuator(WateringScheduler wateringScheduler, Actuator actuator) {
        if (actuator.getField() == null) {
            throw new IllegalArgumentException("Actuator is not assigned to a field");
        }

        WateringScheduler existingScheduler = wateringSchedulerRepository.findByActuator(actuator);
        if (existingScheduler != null) {
            wateringSchedulerRepository.delete(existingScheduler);
            schedulerMap.remove(actuator.getId());
        }

        wateringScheduler.setActuator(actuator);
        actuatorService.changeState(actuator, DeviceState.ON);
        wateringSchedulerRepository.save(wateringScheduler);
        schedulerMap.put(actuator.getId(), wateringScheduler);

        wateringLogService.logWateringStart(actuator.getField().getFarmer(), actuator.getField(), wateringScheduler.getDuration());

        long durationInSeconds = (long) wateringScheduler.getDuration();
        scheduler.schedule(() -> {
            actuatorService.changeState(actuator, DeviceState.OFF);
            measurementService.createWateringMeasurement(wateringScheduler);
            // TODO Do the notification part to the client
            wateringLogService.logWateringEnd(actuator.getField().getFarmer(), actuator.getField(), wateringScheduler);
            schedulerMap.remove(actuator.getId());
        }, durationInSeconds, TimeUnit.SECONDS);

        // TODO Do the notification part to the client
    }

    // TODO In case of intelligent watering, we can either cancel the scheduler or delete it
    // For example : if the farmer wants to cancel the intelligent watering in progress but keep the scheduler for later use
    public void deleteWateringScheduler(WateringScheduler wateringScheduler) {
        Actuator actuator = wateringScheduler.getActuator();
        if (actuator.getState() == DeviceState.ON) {
            actuatorService.changeState(actuator, DeviceState.OFF);
            // log the cancellation
            wateringLogService.logWateringCancellation(actuator.getField().getFarmer(), actuator.getField(), wateringScheduler);
            long durationInSeconds = ChronoUnit.SECONDS.between(wateringScheduler.getBeginDate(), LocalDateTime.now());
            wateringScheduler.setDuration(durationInSeconds);
            measurementService.createWateringMeasurement(wateringScheduler);
        }
        wateringSchedulerRepository.delete(wateringScheduler);
        schedulerMap.remove(actuator.getId());
    }

    public WateringScheduler findByActuator(Actuator actuator) {
        return wateringSchedulerRepository.findByActuator(actuator);
    }
}
