package fr.miage.acm.wateringservice.device.actuator.watering.scheduler;

import fr.miage.acm.wateringservice.device.actuator.Actuator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WateringSchedulerRepository extends JpaRepository<WateringScheduler, UUID> {
    WateringScheduler findByActuator(Actuator actuator);
}
