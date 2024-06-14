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
    private UUID farmerId;

    private String message;

    public WateringLog() {
        // Default constructor required by JPA
    }

    public WateringLog(String message, UUID farmerId) {
        this.dateTime = LocalDateTime.now();
        this.message = message;
        this.farmerId = farmerId;
    }

    // To String
    @Override
    public String toString() {
        return "WateringLog{" +
                ", dateTime=" + dateTime +
                ", message='" + message + '\'' +
                ", farmerId='" + farmerId + '\'' +
                '}';
    }

}
