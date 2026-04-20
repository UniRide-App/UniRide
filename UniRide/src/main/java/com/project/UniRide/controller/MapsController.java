package com.project.uniride.controller;

import com.project.uniride.dto.DTOs.ApiResponse;
import com.project.uniride.service.GoogleMapsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maps")
@CrossOrigin(origins = "*")
public class MapsController {

    private final GoogleMapsService mapsService;

    public MapsController(GoogleMapsService mapsService) { this.mapsService = mapsService; }

    /** GET /api/maps/autocomplete?input=Tiger&lat=30.41&lng=-91.18 */
    @GetMapping("/autocomplete")
    public ResponseEntity<String> autocomplete(
            @RequestParam String input,
            @RequestParam(defaultValue = "30.4133") double lat,
            @RequestParam(defaultValue = "-91.1800") double lng) {
        return ResponseEntity.ok(mapsService.autocomplete(input, lat, lng));
    }

    /** GET /api/maps/directions?oLat=...&oLng=...&dLat=...&dLng=... */
    @GetMapping("/directions")
    public ResponseEntity<String> directions(
            @RequestParam double oLat, @RequestParam double oLng,
            @RequestParam double dLat, @RequestParam double dLng) {
        return ResponseEntity.ok(mapsService.getDirections(oLat, oLng, dLat, dLng));
    }

    /** GET /api/maps/distance?oLat=...&oLng=...&dLat=...&dLng=... */
    @GetMapping("/distance")
    public ResponseEntity<String> distance(
            @RequestParam double oLat, @RequestParam double oLng,
            @RequestParam double dLat, @RequestParam double dLng) {
        return ResponseEntity.ok(mapsService.getDistanceMatrix(oLat, oLng, dLat, dLng));
    }

    /** GET /api/maps/geocode?address=Tiger+Stadium */
    @GetMapping("/geocode")
    public ResponseEntity<String> geocode(@RequestParam String address) {
        return ResponseEntity.ok(mapsService.geocode(address));
    }
}
