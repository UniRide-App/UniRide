package com.project.uniride.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Google Maps API integration for:
 * - Places Autocomplete (search bar suggestions)
 * - Geocoding (address → lat/lng)
 * - Directions (route between two points)
 * - Distance Matrix (ETA calculations)
 *
 * SETUP: Add your API key to application.properties:
 *   google.maps.api-key=YOUR_KEY_HERE
 *
 * Enable these APIs in Google Cloud Console:
 *   - Maps JavaScript API (for frontend map)
 *   - Places API (for autocomplete)
 *   - Directions API (for routing)
 *   - Geocoding API (for address lookup)
 *   - Distance Matrix API (for ETAs)
 */
@Service
public class GoogleMapsService {

    @Value("${google.maps.api-key:NOT_SET}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE = "https://maps.googleapis.com/maps/api";

    /** Get place autocomplete suggestions for the search bar */
    public String autocomplete(String input, double lat, double lng) {
        String url = BASE + "/place/autocomplete/json"
                + "?input=" + input
                + "&location=" + lat + "," + lng
                + "&radius=5000"
                + "&key=" + apiKey;
        return restTemplate.getForObject(url, String.class);
    }

    /** Get directions between two points */
    public String getDirections(double originLat, double originLng,
                                 double destLat, double destLng) {
        String url = BASE + "/directions/json"
                + "?origin=" + originLat + "," + originLng
                + "&destination=" + destLat + "," + destLng
                + "&key=" + apiKey;
        return restTemplate.getForObject(url, String.class);
    }

    /** Get distance and duration between two points */
    public String getDistanceMatrix(double originLat, double originLng,
                                     double destLat, double destLng) {
        String url = BASE + "/distancematrix/json"
                + "?origins=" + originLat + "," + originLng
                + "&destinations=" + destLat + "," + destLng
                + "&key=" + apiKey;
        return restTemplate.getForObject(url, String.class);
    }

    /** Geocode an address to lat/lng */
    public String geocode(String address) {
        String url = BASE + "/geocode/json"
                + "?address=" + address
                + "&key=" + apiKey;
        return restTemplate.getForObject(url, String.class);
    }
}
