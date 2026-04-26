package com.project.uniride.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
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

    private FirebaseToken verifyToken(String authHeader) throws Exception {
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new IllegalArgumentException("Missing or invalid Authorization header");
        return FirebaseAuth.getInstance().verifyIdToken(authHeader.substring(7));
    }

    /** POST /api/auth/register
     *  Header: Authorization: Bearer <firebase-id-token>
     *  Body:   { firstName, lastName, phoneNumber }
     *  Creates the MongoDB user profile after Firebase account creation.
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody RegisterRequest req) {
        try {
            FirebaseToken token = verifyToken(authHeader);
            User user = userService.registerWithFirebase(token.getUid(), token.getEmail(), req);
            return ResponseEntity.ok(ApiResponse.ok("Registration complete!", user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Server error: " + e.getMessage()));
        }
    }

    /** POST /api/auth/login
     *  Header: Authorization: Bearer <firebase-id-token>
     *  Looks up the MongoDB profile by Firebase UID.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<User>> login(
            @RequestHeader("Authorization") String authHeader) {
        try {
            FirebaseToken token = verifyToken(authHeader);
            User user = userService.loginWithFirebase(token.getUid());
            return ResponseEntity.ok(ApiResponse.ok("Login successful", user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Server error: " + e.getMessage()));
        }
    }
}
