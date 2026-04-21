package com.project.uniride.Web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.uniride.Entities.CarsEntity;
import com.project.uniride.Repositories.CarsRepository;

//Author: Hannah Lowery
//Handles Http request
@RestController
@RequestMapping("/cars")
public class CarsController {
    private final CarsRepository repository;

    public CarsController(CarsRepository repository) {
        this.repository = repository;
    }

    // Get all cars
    @GetMapping
    public Iterable<CarsEntity> getCars() {
        return repository.findAll();
    }

    // Get a single car by ID
    @GetMapping("/{id}")
    public ResponseEntity<CarsEntity> getCarById(@PathVariable Long id) {
        return repository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // Add a new car
    @PostMapping
    public CarsEntity addCar(@RequestBody CarsEntity car) {
        return repository.save(car);
    }

    // Update a car
   @PutMapping("/{id}")
    public ResponseEntity<CarsEntity> updateCar(@PathVariable Long id, @RequestBody CarsEntity car) {
    return repository.findById(id)
        .map(existingCar -> {
            existingCar.setBrand(car.getBrand());
            existingCar.setModel(car.getModel());
            existingCar.setColor(car.getColor());
            existingCar.setLicensePlate(car.getLicensePlate());
            existingCar.setModelYear(car.getModelYear());
            return ResponseEntity.ok(repository.save(existingCar));
        })
        .orElse(ResponseEntity.notFound().build());
    }

    // Delete a car
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
