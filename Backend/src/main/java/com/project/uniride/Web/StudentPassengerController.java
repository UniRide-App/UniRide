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
import com.project.uniride.Implementation.EmailValidationService;
import com.project.uniride.Implementation.VerificationService;
import com.project.uniride.Repositories.StudentPassengerRepository;

@RestController
@RequestMapping("/studentpassengers")
public class StudentPassengerController {
    private final StudentPassengerRepository repository;
    private final VerificationService verificationService;
    private final EmailValidationService emailValidationService;

    public StudentPassengerController(StudentPassengerRepository repository,
                                      VerificationService verificationService,
                                      EmailValidationService emailValidationService) {
        this.repository = repository;
        this.verificationService = verificationService;
        this.emailValidationService = emailValidationService;
    }

    @GetMapping
    public Iterable<StudentPassengerEntity> getStudentPassengers() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentPassengerEntity> getStudentPassengerById(@PathVariable Long id) {
        return repository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerStudentPassenger(
            @RequestBody StudentPassengerEntity studentPassenger) {
        if (!emailValidationService.isCollegeEmail(studentPassenger.getEmail())) {
            return ResponseEntity.badRequest()
                .body("Registration failed! You must use a college email address!");
        }
        StudentPassengerEntity existing = repository.findByEmail(studentPassenger.getEmail());
        if (existing != null) {
            return ResponseEntity.badRequest().body("Email already registered!");
        }
        verificationService.sendPassengerVerification(studentPassenger);
        return ResponseEntity.ok("Registration successful! Please check your college email to verify your account.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentPassengerEntity> updateStudentPassenger(
            @PathVariable Long id, @RequestBody StudentPassengerEntity studentPassenger) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentPassenger(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}