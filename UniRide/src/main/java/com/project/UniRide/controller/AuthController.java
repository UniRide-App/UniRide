package com.project.uniride.controller;

import com.project.uniride.dto.DTOs.*;
import com.project.uniride.model.User;
import com.project.uniride.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) { this.userService = userService; }

    /** POST /api/auth/register — Register with LSU email, sends OTP */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@RequestBody RegisterRequest req) {
        try {
            User user = userService.register(req);
            return ResponseEntity.ok(ApiResponse.ok("OTP sent to " + req.getEmail(), user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Server error: " + e.getMessage()));
        }
    }

    /** POST /api/auth/verify-otp — Verify the 4-digit OTP code */
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<User>> verifyOtp(@RequestBody OtpVerifyRequest req) {
        try {
            User user = userService.verifyOtp(req);
            return ResponseEntity.ok(ApiResponse.ok("Email verified!", user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Server error: " + e.getMessage()));
        }
    }

    /** POST /api/auth/login — Log in with email + password */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<User>> login(@RequestBody LoginRequest req) {
        try {
            User user = userService.login(req);
            return ResponseEntity.ok(ApiResponse.ok("Login successful", user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Server error: " + e.getMessage()));
        }
    }

    /** POST /api/auth/resend-otp?email=... — Resend OTP code */
    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse<String>> resendOtp(@RequestParam String email) {
        try {
            userService.resendOtp(email);
            return ResponseEntity.ok(ApiResponse.ok("New OTP sent", email));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Server error: " + e.getMessage()));
        }
    }
}
