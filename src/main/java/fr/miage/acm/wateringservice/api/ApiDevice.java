package fr.miage.acm.wateringservice.api;

import fr.miage.acm.wateringservice.device.DeviceState;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public abstract class ApiDevice {

    private UUID id;

    private DeviceState state;

    private ApiFarmer farmer;

    public ApiDevice(ApiFarmer farmer) {
        this.state = DeviceState.NOT_ASSIGNED;
        this.farmer = farmer;
    }

    public ApiDevice() {
        // Default constructor required by JPA
    }
}
