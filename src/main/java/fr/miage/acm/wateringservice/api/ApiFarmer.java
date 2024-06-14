package fr.miage.acm.wateringservice.api;

import fr.miage.acm.wateringservice.farmer.Farmer;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ApiFarmer {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer fieldSize;

    public ApiFarmer(Farmer farmer) {
        this.id = farmer.getId();
        this.firstName = farmer.getFirstName();
        this.lastName = farmer.getLastName();
        this.email = farmer.getEmail();
        this.fieldSize = farmer.getFieldSize();
    }

    public ApiFarmer() {
    }

    public ApiFarmer(UUID id, String firstName, String lastName, String email, Integer fieldSize) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.fieldSize = fieldSize;
    }


    @Override
    public String toString() {
        return "Farmer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", fieldSize=" + fieldSize +
                '}';
    }
}
