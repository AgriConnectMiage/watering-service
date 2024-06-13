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

    public boolean isWateringInProgress(WateringScheduler wateringScheduler) {
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
                scheduleWatering(actuator, wateringScheduler, delay);
            }
        }
    }

    private void scheduleWatering(Actuator actuator, WateringScheduler wateringScheduler, long delay) {
        this.scheduler.schedule(() -> {
            actuatorService.changeState(actuator, DeviceState.OFF);
            measurementService.createWateringMeasurement(wateringScheduler);
            wateringLogService.logWateringEnd(actuator.getField().getFarmer(), actuator.getField(), wateringScheduler);
            schedulerMap.remove(actuator.getId());
            // set beginDate and endDate to null but keep the scheduler
            wateringScheduler.setBeginDate(null);
            wateringScheduler.setEndDate(null);
            wateringSchedulerRepository.save(wateringScheduler);
        }, delay, TimeUnit.SECONDS);
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
        scheduleWatering(actuator, wateringScheduler, durationInSeconds);

        // TODO Do the notification part to the client
    }

    // add intelligent watering scheduler (with humidity threshold)
    public void addIntelligentWateringSchedulerToActuator(WateringScheduler wateringScheduler, Actuator actuator) {
        if (actuator.getField() == null) {
            throw new IllegalArgumentException("Actuator is not assigned to a field");
        }

        WateringScheduler existingScheduler = wateringSchedulerRepository.findByActuator(actuator);
        if (existingScheduler != null) {
            wateringSchedulerRepository.delete(existingScheduler);
            schedulerMap.remove(actuator.getId());
        }

        wateringScheduler.setActuator(actuator);
        wateringSchedulerRepository.save(wateringScheduler);
        // TODO Do the notification part to the client
    }


    // cancel watering in progress but keep the scheduler for later use : 2 cases : if humidity threshold is not null, we keep the scheduler and set the beginDate and endDate at null, else we delete it
    public void cancelWateringInProgress(Actuator actuator) {
        WateringScheduler wateringScheduler = schedulerMap.get(actuator.getId());
        if (wateringScheduler != null) {
            if (wateringScheduler.getHumidityThreshold() != null) {
                wateringScheduler.setBeginDate(null);
                wateringScheduler.setEndDate(null);
                wateringSchedulerRepository.save(wateringScheduler);
            } else {
                deleteWateringScheduler(wateringScheduler);
            }
            actuatorService.changeState(actuator, DeviceState.OFF);
            schedulerMap.remove(actuator.getId());
            // TODO Do the notification part to the client
            // log the cancellation
            wateringLogService.logWateringCancellation(actuator.getField().getFarmer(), actuator.getField(), wateringScheduler);
        }
    }

    public void deleteWateringScheduler(WateringScheduler wateringScheduler) {
        wateringSchedulerRepository.delete(wateringScheduler);
    }

    public WateringScheduler findByActuator(Actuator actuator) {
        return wateringSchedulerRepository.findByActuator(actuator);
    }

    // trigger watering scheduler by setting beginDate to now and endDate to now + duration
    public void triggerIntelligentWatering(WateringScheduler wateringScheduler) {
        LocalDateTime now = LocalDateTime.now();
        wateringScheduler.setBeginDate(now);
        wateringScheduler.setEndDate(now.plusSeconds((long) wateringScheduler.getDuration()));
        wateringSchedulerRepository.save(wateringScheduler);

        Actuator actuator = wateringScheduler.getActuator();
        actuatorService.changeState(actuator, DeviceState.ON);
        schedulerMap.put(actuator.getId(), wateringScheduler);

        long delay = ChronoUnit.SECONDS.between(now, wateringScheduler.getEndDate());
        scheduleWatering(actuator, wateringScheduler, delay);
    }


}
