package fr.miage.acm.wateringservice.field;

import fr.miage.acm.wateringservice.farmer.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FieldRepository extends JpaRepository<Field, UUID> {
    List<Field> findByFarmer(Farmer farmer);

    void deleteByFarmer(Farmer farmer);
}
