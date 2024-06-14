package fr.miage.acm.wateringservice.client;

import fr.miage.acm.wateringservice.api.ApiWateringScheduler;
import fr.miage.acm.wateringservice.device.measurement.Measurement;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "measurement-service")
public interface MeasurementServiceClient {

    @PostMapping("/measurements/watering")
    Measurement createWateringMeasurement(@RequestBody ApiWateringScheduler apiWateringScheduler);
}
