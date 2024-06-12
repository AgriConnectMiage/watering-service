package fr.miage.acm.wateringservice.device.measurement;

import fr.miage.acm.wateringservice.device.Device;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private LocalDateTime dateTime;

    private UUID farmerId;
    private String fieldCoord;
    private UUID deviceId;

    @Column(columnDefinition = "NUMERIC(5,1)")
    private Float humidity;
    @Column(columnDefinition = "NUMERIC(5,1)")
    private Float temperature;
    @Column(columnDefinition = "NUMERIC(5,1)")
    private Float wateringDuration;

    public Measurement(UUID id, LocalDateTime dateTime, Device device, Float humidity, Float temperature, Float wateringDuration) {
        this.id = id;
        this.dateTime = dateTime;
        this.deviceId = device.getId();
        this.farmerId = device.getFarmer().getId();
        this.humidity = humidity;
        this.temperature = temperature;
        this.wateringDuration = wateringDuration;
    }

    public Measurement() {
        // Default constructor required by JPA
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", sourceId=" + deviceId +
                ", farmerId='" + farmerId + '\'' +
                ", humidity=" + humidity +
                ", temperature=" + temperature +
                ", duration=" + wateringDuration +
                '}';
    }
}
