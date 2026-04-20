package com.project.uniride.controller;

import com.project.uniride.dto.DTOs.*;
import com.project.uniride.model.Ride;
import com.project.uniride.service.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rides")
@CrossOrigin(origins = "*")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) { this.rideService = rideService; }

    @PostMapping("/request")
    public ResponseEntity<ApiResponse<Ride>> request(@RequestParam String riderId, @RequestBody RideRequest req) {
        try { return ResponseEntity.ok(ApiResponse.ok("Finding a driver...", rideService.requestRide(riderId, req))); }
        catch (Exception e) { return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage())); }
    }

    @PostMapping("/{rideId}/accept")
    public ResponseEntity<ApiResponse<Ride>> accept(@PathVariable String rideId, @RequestParam String driverId) {
        try { return ResponseEntity.ok(ApiResponse.ok("Ride accepted!", rideService.acceptRide(driverId, rideId))); }
        catch (Exception e) { return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage())); }
    }

    @PostMapping("/{rideId}/arrived")
    public ResponseEntity<ApiResponse<Ride>> arrived(@PathVariable String rideId, @RequestParam String driverId) {
        try { return ResponseEntity.ok(ApiResponse.ok(rideService.driverArrived(driverId, rideId))); }
        catch (Exception e) { return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage())); }
    }

    @PostMapping("/{rideId}/start")
    public ResponseEntity<ApiResponse<Ride>> start(@PathVariable String rideId, @RequestParam String driverId) {
        try { return ResponseEntity.ok(ApiResponse.ok(rideService.startTrip(driverId, rideId))); }
        catch (Exception e) { return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage())); }
    }

    @PostMapping("/{rideId}/complete")
    public ResponseEntity<ApiResponse<Ride>> complete(@PathVariable String rideId, @RequestParam String driverId) {
        try { return ResponseEntity.ok(ApiResponse.ok("Trip completed!", rideService.completeTrip(driverId, rideId))); }
        catch (Exception e) { return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage())); }
    }

    @PostMapping("/{rideId}/cancel")
    public ResponseEntity<ApiResponse<Ride>> cancel(@PathVariable String rideId, @RequestParam String userId, @RequestParam(required = false) String reason) {
        try { return ResponseEntity.ok(ApiResponse.ok(rideService.cancelRide(userId, rideId, reason))); }
        catch (Exception e) { return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage())); }
    }

    @PostMapping("/{rideId}/rate")
    public ResponseEntity<ApiResponse<Ride>> rate(@PathVariable String rideId, @RequestParam String userId, @RequestBody RatingRequest req) {
        try { return ResponseEntity.ok(ApiResponse.ok(rideService.rateRide(userId, rideId, req))); }
        catch (Exception e) { return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage())); }
    }

    @GetMapping("/{rideId}")
    public ResponseEntity<ApiResponse<Ride>> get(@PathVariable String rideId) {
        try { return ResponseEntity.ok(ApiResponse.ok(rideService.findById(rideId))); }
        catch (Exception e) { return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage())); }
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<Ride>>> history(@RequestParam String userId) {
        return ResponseEntity.ok(ApiResponse.ok(rideService.getHistory(userId)));
    }
}
