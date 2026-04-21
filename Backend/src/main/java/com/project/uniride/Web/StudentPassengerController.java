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

import com.project.uniride.Entities.StudentPassengerEntity;
import com.project.uniride.Repositories.StudentPassengerRepository;

//Author: Hannah Lowery
//Handles Http request

@RestController
@RequestMapping("/studentpassengers")
public class StudentPassengerController {
    private final StudentPassengerRepository repository;

    public StudentPassengerController(StudentPassengerRepository repository) {
        this.repository = repository;
    }

    // Get all student passengers
    @GetMapping
    public Iterable<StudentPassengerEntity> getStudentPassengers() {
        return repository.findAll();
    }

    // Get a single student passenger by ID
    @GetMapping("/{id}")
    public ResponseEntity<StudentPassengerEntity> getStudentPassengerById(@PathVariable Long id) {
        return repository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // Register a new student passenger
    @PostMapping
    public StudentPassengerEntity registerStudentPassenger(@RequestBody StudentPassengerEntity studentPassenger) {
        return repository.save(studentPassenger);
    }

    // Update a student passenger
    @PutMapping("/{id}")
    public ResponseEntity<StudentPassengerEntity> updateStudentPassenger(@PathVariable Long id, @RequestBody StudentPassengerEntity studentPassenger) {
        return repository.findById(id)
            .map(existingStudent -> {
                existingStudent.setFirstName(studentPassenger.getFirstName());
                existingStudent.setLastName(studentPassenger.getLastName());
                existingStudent.setEmail(studentPassenger.getEmail());
                existingStudent.setSchool(studentPassenger.getSchool());
                existingStudent.setPassword(studentPassenger.getpassword());
                return ResponseEntity.ok(repository.save(existingStudent));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // Delete a student passenger
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentPassenger(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}