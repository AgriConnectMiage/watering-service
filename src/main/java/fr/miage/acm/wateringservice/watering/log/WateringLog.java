package fr.miage.acm.wateringservice.watering.log;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
public class WateringLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private LocalDateTime dateTime;

    private String message;

    public WateringLog() {
        // Default constructor required by JPA
    }

    // To String
    @Override
    public String toString() {
        return "WateringLog{" +
                ", dateTime=" + dateTime +
                ", message='" + message + '\'' +
                '}';
    }

}
