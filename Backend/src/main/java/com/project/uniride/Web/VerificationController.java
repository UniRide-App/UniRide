package com.project.uniride.Web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.uniride.Implementation.VerificationService;

@RestController
@RequestMapping("/verify")
public class VerificationController {
    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @GetMapping
    public ResponseEntity<String> verify(@RequestParam String token) {
        if (verificationService.verifyPassenger(token)) {
            return ResponseEntity.ok("Student passenger verified successfully!");
        }
        if (verificationService.verifyDriver(token)) {
            return ResponseEntity.ok("Student driver verified successfully!");
        }
        return ResponseEntity.badRequest().body("Invalid or expired token!");
    }
}
