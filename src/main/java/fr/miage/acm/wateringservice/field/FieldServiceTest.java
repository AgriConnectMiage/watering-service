package fr.miage.acm.wateringservice.field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FieldServiceTest {
    private final FieldService fieldService;

    public FieldServiceTest(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    public FieldServiceTest() {
        this.fieldService = null;
    }


    public void getFields() {
        System.out.println("Fields: " + fieldService.findAll());
    }
}
