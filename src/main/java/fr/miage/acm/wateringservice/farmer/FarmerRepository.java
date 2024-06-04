package fr.miage.acm.wateringservice.farmer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FarmerRepository extends JpaRepository<Farmer, UUID> {

    Farmer findByEmail(String email);
}
