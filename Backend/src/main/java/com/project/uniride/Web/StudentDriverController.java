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

import com.project.uniride.Entities.StudentDriverEntity;
import com.project.uniride.Repositories.StudentDriverRepository;

//Author: Hannah Lowery
//Handles Http request

@RestController
@RequestMapping("/studentdrivers")
public class StudentDriverController {
    private final StudentDriverRepository repository;

    public StudentDriverController(StudentDriverRepository repository) {
        this.repository = repository;
    }

    // Get all student drivers
    @GetMapping
    public Iterable<StudentDriverEntity> getStudentDrivers() {
        return repository.findAll();
    }

    // Get a single student driver by ID
    @GetMapping("/{id}")
    public ResponseEntity<StudentDriverEntity> getStudentDriverById(@PathVariable Long id) {
        return repository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // Register a new student driver
    @PostMapping
    public StudentDriverEntity registerStudentDriver(@RequestBody StudentDriverEntity studentDriver) {
        return repository.save(studentDriver);
    }

    // Update a student driver
    @PutMapping("/{id}")
    public ResponseEntity<StudentDriverEntity> updateStudentDriver(@PathVariable Long id, @RequestBody StudentDriverEntity studentDriver) {
        return repository.findById(id)
            .map(existingDriver -> {
                existingDriver.setFirstName(studentDriver.getFirstName());
                existingDriver.setLastName(studentDriver.getLastName());
                existingDriver.setEmail(studentDriver.getEmail());
                existingDriver.setSchool(studentDriver.getSchool());
                existingDriver.setPassword(studentDriver.getpassword());
                return ResponseEntity.ok(repository.save(existingDriver));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // Delete a student driver
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentDriver(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}