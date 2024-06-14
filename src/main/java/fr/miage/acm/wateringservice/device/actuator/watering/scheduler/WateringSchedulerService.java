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
import java.util.concurrent.*;

@Service
public class WateringSchedulerService {

    private final WateringSchedulerRepository wateringSchedulerRepository;
    private final ActuatorService actuatorService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final WateringLogService wateringLogService;
    private final MeasurementService measurementService;
    private final ConcurrentHashMap<UUID, ScheduledFuture<?>> schedulerMap = new ConcurrentHashMap<>();

    public WateringSchedulerService(WateringSchedulerRepository wateringSchedulerRepository, ActuatorService actuatorService, WateringLogService wateringLogService, MeasurementService measurementService) {
        this.wateringSchedulerRepository = wateringSchedulerRepository;
        this.actuatorService = actuatorService;
        this.wateringLogService = wateringLogService;
        this.measurementService = measurementService;
    }

    public boolean isWateringInProgress(WateringScheduler wateringScheduler) {
        LocalDateTime now = LocalDateTime.now();
        if (wateringScheduler.getBeginDate() == null || wateringScheduler.getEndDate() == null) {
            return false;
        }
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

                long delay = ChronoUnit.SECONDS.between(now, wateringScheduler.getEndDate());
                ScheduledFuture<?> future = scheduleWatering(actuator, wateringScheduler, delay);
                schedulerMap.put(actuator.getId(), future);
            }
        }
    }

    private ScheduledFuture<?> scheduleWatering(Actuator actuator, WateringScheduler wateringScheduler, long delay) {
        return this.scheduler.schedule(() -> {
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
            cancelScheduledTask(actuator.getId());
        }

        wateringScheduler.setActuator(actuator);
        actuatorService.changeState(actuator, DeviceState.ON);
        wateringSchedulerRepository.save(wateringScheduler);

        wateringLogService.logWateringStart(actuator.getField().getFarmer(), actuator.getField(), wateringScheduler.getDuration());

        long durationInSeconds = (long) wateringScheduler.getDuration();
        ScheduledFuture<?> future = scheduleWatering(actuator, wateringScheduler, durationInSeconds);
        schedulerMap.put(actuator.getId(), future);
    }

    // add intelligent watering scheduler (with humidity threshold)
    public void addIntelligentWateringSchedulerToActuator(WateringScheduler wateringScheduler, Actuator actuator) {
        if (actuator.getField() == null) {
            throw new IllegalArgumentException("Actuator is not assigned to a field");
        }

        WateringScheduler existingScheduler = wateringSchedulerRepository.findByActuator(actuator);
        if (existingScheduler != null) {
            wateringSchedulerRepository.delete(existingScheduler);
            cancelScheduledTask(actuator.getId());
        }

        wateringScheduler.setActuator(actuator);
        wateringSchedulerRepository.save(wateringScheduler);
    }

    public void cancelWateringInProgress(Actuator actuator) {
        ScheduledFuture<?> future = schedulerMap.get(actuator.getId());
        if (future != null) {
            future.cancel(true);
            schedulerMap.remove(actuator.getId());
            actuatorService.changeState(actuator, DeviceState.OFF);

            WateringScheduler wateringScheduler = wateringSchedulerRepository.findByActuator(actuator);
            if (wateringScheduler != null) {
                if (wateringScheduler.getHumidityThreshold() != null) {
                    wateringScheduler.setBeginDate(null);
                    wateringScheduler.setEndDate(null);
                    wateringSchedulerRepository.save(wateringScheduler);
                } else {
                    deleteWateringScheduler(wateringScheduler);
                }
                wateringLogService.logWateringCancellation(actuator.getField().getFarmer(), actuator.getField(), wateringScheduler);
            }
        }
    }

    public void deleteWateringScheduler(WateringScheduler wateringScheduler) {
        wateringSchedulerRepository.delete(wateringScheduler);
    }

    public WateringScheduler findByActuator(Actuator actuator) {
        return wateringSchedulerRepository.findByActuator(actuator);
    }

    public void triggerIntelligentWatering(WateringScheduler wateringScheduler) {
        LocalDateTime now = LocalDateTime.now();
        wateringScheduler.setBeginDate(now);
        wateringScheduler.setEndDate(now.plusSeconds((long) wateringScheduler.getDuration()));
        wateringSchedulerRepository.save(wateringScheduler);

        Actuator actuator = wateringScheduler.getActuator();
        actuatorService.changeState(actuator, DeviceState.ON);
        wateringLogService.logWateringStart(actuator.getField().getFarmer(), actuator.getField(), wateringScheduler.getDuration());
        long delay = ChronoUnit.SECONDS.between(now, wateringScheduler.getEndDate());
        ScheduledFuture<?> future = scheduleWatering(actuator, wateringScheduler, delay);
        schedulerMap.put(actuator.getId(), future);
    }

    private void cancelScheduledTask(UUID actuatorId) {
        ScheduledFuture<?> future = schedulerMap.get(actuatorId);
        if (future != null) {
            future.cancel(true);
            schedulerMap.remove(actuatorId);
        }
    }
}
