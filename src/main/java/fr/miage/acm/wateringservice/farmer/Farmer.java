package fr.miage.acm.wateringservice.farmer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
public class Farmer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Integer fieldSize;

    public Farmer(String firstName, String lastName, String email, String password, int fieldSize) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.fieldSize = fieldSize;
    }

    public Farmer() {
        // Default constructor required by JPA
    }

    @Override
    public String toString() {
        return "Farmer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", fieldSize=" + fieldSize +
                '}';
    }
}
