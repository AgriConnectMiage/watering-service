package fr.miage.acm.wateringservice.device.actuator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
public class WateringEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private LocalDateTime beginDate;
    private LocalDateTime endDate;
    private float duration;
    private float humidityThreshold;

    public WateringEvent(LocalDateTime beginDate, LocalDateTime endDate, float duration, float humidityThreshold) {
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.duration = duration;
        this.humidityThreshold = humidityThreshold;
    }

    public WateringEvent() {
        // Default constructor required by JPA
    }

    @Override
    public String toString() {
        return "WateringEvent{" +
                "id=" + id +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", duration=" + duration +
                ", humidityThreshold=" + humidityThreshold +
                '}';
    }
}
