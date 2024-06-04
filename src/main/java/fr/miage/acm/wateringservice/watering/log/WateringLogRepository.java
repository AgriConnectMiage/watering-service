package fr.miage.acm.wateringservice.watering.log;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WateringLogRepository extends JpaRepository<WateringLog, UUID> {
}
