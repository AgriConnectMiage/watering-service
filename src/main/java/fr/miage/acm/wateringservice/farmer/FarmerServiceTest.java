package fr.miage.acm.wateringservice.farmer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FarmerServiceTest {

    private final FarmerService farmerService;

    public FarmerServiceTest(FarmerService farmerService) {
        this.farmerService = farmerService;
    }


}