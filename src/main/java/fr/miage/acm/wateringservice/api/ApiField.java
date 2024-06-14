package fr.miage.acm.wateringservice.api;

import fr.miage.acm.wateringservice.field.Field;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ApiField {

    public UUID id;
    public Integer xcoord;
    public Integer ycoord;
    public ApiFarmer farmer;

    public ApiField(Field field) {
        this.id = field.getId();
        this.xcoord = field.getXcoord();
        this.ycoord = field.getYcoord();
        this.farmer = new ApiFarmer(field.getFarmer());
    }

    public String getCoord() {
        return xcoord + "," + ycoord;
    }

    public ApiField() {
    }

    public ApiField(UUID id, Integer xcoord, Integer ycoord, ApiFarmer farmer) {
        this.id = id;
        this.xcoord = xcoord;
        this.ycoord = ycoord;
        this.farmer = farmer;
    }

}
