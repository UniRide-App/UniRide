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

import com.project.uniride.Entities.RidesEntity;
import com.project.uniride.Repositories.RidesRepository;

//Author: Hannah Lowery
//Handles Http request

@RestController
@RequestMapping("/rides")
public class RidesController {
    private final RidesRepository repository;

    public RidesController(RidesRepository repository) {
        this.repository = repository;
    }

    // Get all rides
    @GetMapping
    public Iterable<RidesEntity> getRides() {
        return repository.findAll();
    }

    // Get a single ride by ID
    @GetMapping("/{id}")
    public ResponseEntity<RidesEntity> getRideById(@PathVariable Long id) {
        return repository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // Create a new ride
    @PostMapping
    public RidesEntity createRide(@RequestBody RidesEntity ride) {
        return repository.save(ride);
    }

    // Update a ride
    @PutMapping("/{id}")
    public ResponseEntity<RidesEntity> updateRide(@PathVariable Long id, @RequestBody RidesEntity ride) {
        return repository.findById(id)
            .map(existingRide -> {
                existingRide.setPickupLocation(ride.getPickupLocation());
                existingRide.setDropoffLocation(ride.getDropoffLocation());
                existingRide.setFare(ride.getFare());
                existingRide.setStatus(ride.getStatus());
                return ResponseEntity.ok(repository.save(existingRide));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // Delete a ride
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRide(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
