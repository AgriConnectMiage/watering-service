package fr.miage.acm.wateringservice.device.actuator.watering.scheduler;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WateringSchedulerRepository extends JpaRepository<WateringScheduler, UUID> {
}
