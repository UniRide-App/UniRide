package com.project.uniride.Web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.uniride.Entities.TipsEntity;
import com.project.uniride.Repositories.TipsRepository;

//Author: Hannah Lowery
//Handles Http request

@RestController
@RequestMapping("/tips")
public class TipsController {
    private final TipsRepository repository;

    public TipsController(TipsRepository repository) {
        this.repository = repository;
    }

    // Get all tips
    @GetMapping
    public Iterable<TipsEntity> getTips() {
        return repository.findAll();
    }

    // Get a single tip by ID
    @GetMapping("/{id}")
    public ResponseEntity<TipsEntity> getTipById(@PathVariable Long id) {
        return repository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // Create a new tip
    @PostMapping
    public TipsEntity createTip(@RequestBody TipsEntity tip) {
        return repository.save(tip);
    }

    // Delete a tip
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTip(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}