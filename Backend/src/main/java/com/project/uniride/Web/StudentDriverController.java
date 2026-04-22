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
import com.project.uniride.Implementation.EmailValidationService;
import com.project.uniride.Implementation.VerificationService;
import com.project.uniride.Repositories.StudentDriverRepository;

@RestController
@RequestMapping("/studentdrivers")
public class StudentDriverController {
    private final StudentDriverRepository repository;
    private final VerificationService verificationService;
    private final EmailValidationService emailValidationService;

    public StudentDriverController(StudentDriverRepository repository,
                                   VerificationService verificationService,
                                   EmailValidationService emailValidationService) {
        this.repository = repository;
        this.verificationService = verificationService;
        this.emailValidationService = emailValidationService;
    }

    @GetMapping
    public Iterable<StudentDriverEntity> getStudentDrivers() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDriverEntity> getStudentDriverById(@PathVariable Long id) {
        return repository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerStudentDriver(
            @RequestBody StudentDriverEntity studentDriver) {
        if (!emailValidationService.isCollegeEmail(studentDriver.getEmail())) {
            return ResponseEntity.badRequest()
                .body("Registration failed! You must use a college email address!");
        }
        StudentDriverEntity existing = repository.findByEmail(studentDriver.getEmail());
        if (existing != null) {
            return ResponseEntity.badRequest().body("Email already registered!");
        }
        verificationService.sendDriverVerification(studentDriver);
        return ResponseEntity.ok("Registration successful! Please check your college email to verify your account.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDriverEntity> updateStudentDriver(
            @PathVariable Long id, @RequestBody StudentDriverEntity studentDriver) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudentDriver(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}