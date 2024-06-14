package fr.miage.acm.wateringservice.device.actuator;

import fr.miage.acm.wateringservice.farmer.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ActuatorRepository extends JpaRepository<Actuator, UUID> {

    void deleteByFarmer(Farmer farmer);

    List<Actuator> findByFarmer(Farmer farmer);
}
