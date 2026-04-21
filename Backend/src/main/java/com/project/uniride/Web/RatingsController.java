package com.project.uniride.Web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.uniride.Entities.RatingsEntity;
import com.project.uniride.Repositories.RatingsRepository;


//Author: Hannah Lowery
//Handles Http request

@RestController
@RequestMapping("/ratings")
public class RatingsController {
    private final RatingsRepository repository;

    public RatingsController(RatingsRepository repository) {
        this.repository = repository;
    }

    // Get all ratings
    @GetMapping
    public Iterable<RatingsEntity> getRatings() {
        return repository.findAll();
    }

    // Get a single rating by ID
    @GetMapping("/{id}")
    public ResponseEntity<RatingsEntity> getRatingById(@PathVariable Long id) {
        return repository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // Create a new rating
    @PostMapping
    public RatingsEntity createRating(@RequestBody RatingsEntity rating) {
        return repository.save(rating);
    }

    // Delete a rating
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}