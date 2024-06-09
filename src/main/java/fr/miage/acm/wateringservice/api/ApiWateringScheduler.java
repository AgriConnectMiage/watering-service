package fr.miage.acm.wateringservice.api;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ApiWateringScheduler {
    private UUID id;

    private ApiActuator actuator;

    private LocalDateTime beginDate;
    private LocalDateTime endDate;

    private float duration;

    private Integer humidityThreshold;

    public ApiWateringScheduler(LocalDateTime beginDate, LocalDateTime endDate, Integer humidityThreshold, ApiActuator apiActuator) {
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.duration = (endDate != null && beginDate != null) ? Timestamp.valueOf(endDate).getTime() - Timestamp.valueOf(beginDate).getTime() : 0;
        this.humidityThreshold = humidityThreshold;
        this.actuator = apiActuator;
    }

    public ApiWateringScheduler(LocalDateTime beginDate, LocalDateTime endDate, ApiActuator apiActuator) {
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.duration = (endDate != null && beginDate != null) ? Timestamp.valueOf(endDate).getTime() - Timestamp.valueOf(beginDate).getTime() : 0;
        this.actuator = apiActuator;

    }

    public ApiWateringScheduler(LocalDateTime beginDate, float duration, Integer humidityThreshold, ApiActuator apiActuator) {
        this.beginDate = beginDate;
        this.duration = duration;
        this.endDate = beginDate.plusSeconds((long) duration);
        this.humidityThreshold = humidityThreshold;
        this.actuator = apiActuator;
    }

    public ApiWateringScheduler(LocalDateTime beginDate, float duration, ApiActuator apiActuator) {
        this.beginDate = beginDate;
        this.duration = duration;
        this.endDate = beginDate.plusSeconds((long) duration);
        this.actuator = apiActuator;
    }

    public ApiWateringScheduler() {
    }

}
