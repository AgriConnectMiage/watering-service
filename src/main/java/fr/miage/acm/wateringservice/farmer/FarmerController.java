package fr.miage.acm.wateringservice.farmer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/farmers")
public class FarmerController {

    private FarmerService farmerService;

    public FarmerController(FarmerService farmerService) {
        this.farmerService = farmerService;
    }
    @GetMapping
    public List<Farmer> getAllFarmers() {
        return farmerService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Farmer> getFarmerById(@PathVariable UUID id) {
        return farmerService.findById(id);
    }

    @PostMapping
    public Farmer createFarmer(@RequestBody Farmer farmer) {
        return farmerService.save(farmer);
    }
}