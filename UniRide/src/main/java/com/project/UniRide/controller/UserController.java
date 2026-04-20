package com.project.uniride.controller;

import com.project.uniride.dto.DTOs.*;
import com.project.uniride.model.GeoLocation;
import com.project.uniride.model.User;
import com.project.uniride.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) { this.userService = userService; }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getProfile(@PathVariable String id) {
        try { return ResponseEntity.ok(ApiResponse.ok(userService.findById(id))); }
        catch (Exception e) { return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage())); }
    }

    /** POST /api/users/{id}/account-type — Switch between Passenger/Driver */
    @PostMapping("/{id}/account-type")
    public ResponseEntity<ApiResponse<User>> switchType(@PathVariable String id, @RequestBody AccountTypeRequest req) {
        try {
            User user = userService.switchAccountType(id, req);
            return ResponseEntity.ok(ApiResponse.ok("Switched to " + req.getAccountType(), user));
        } catch (Exception e) { return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage())); }
    }

    @PostMapping("/{id}/online")
    public ResponseEntity<ApiResponse<User>> goOnline(@PathVariable String id) {
        try { return ResponseEntity.ok(ApiResponse.ok(userService.setOnline(id, true))); }
        catch (Exception e) { return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage())); }
    }

    @PostMapping("/{id}/offline")
    public ResponseEntity<ApiResponse<User>> goOffline(@PathVariable String id) {
        try { return ResponseEntity.ok(ApiResponse.ok(userService.setOnline(id, false))); }
        catch (Exception e) { return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage())); }
    }

    @PutMapping("/{id}/location")
    public ResponseEntity<ApiResponse<User>> updateLocation(@PathVariable String id, @RequestBody LocationUpdate req) {
        try { return ResponseEntity.ok(ApiResponse.ok(userService.updateLocation(id, req))); }
        catch (Exception e) { return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage())); }
    }

    @GetMapping("/nearby-drivers")
    public ResponseEntity<ApiResponse<List<NearbyDriverResponse>>> nearbyDrivers(
            @RequestParam double lat, @RequestParam double lng, @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(ApiResponse.ok(userService.findNearbyDrivers(new GeoLocation(lat, lng), limit)));
    }
}
