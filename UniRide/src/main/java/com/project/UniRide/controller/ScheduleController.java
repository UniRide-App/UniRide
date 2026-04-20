package com.project.uniride.controller;

import com.project.uniride.dto.DTOs.*;
import com.project.uniride.model.Schedule;
import com.project.uniride.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin(origins = "*")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) { this.scheduleService = scheduleService; }

    /** POST /api/schedules/ride — Rider posts a scheduled ride (Schedule Ahead) */
    @PostMapping("/ride")
    public ResponseEntity<ApiResponse<Schedule>> postRideSchedule(@RequestParam String userId, @RequestBody ScheduleRequest req) {
        try { return ResponseEntity.ok(ApiResponse.ok("Schedule posted!", scheduleService.postRideSchedule(userId, req))); }
        catch (Exception e) { return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage())); }
    }

    /** POST /api/schedules/availability — Driver posts availability (Driver Schedule) */
    @PostMapping("/availability")
    public ResponseEntity<ApiResponse<Schedule>> postAvailability(@RequestParam String userId, @RequestBody ScheduleRequest req) {
        try { return ResponseEntity.ok(ApiResponse.ok("Availability posted!", scheduleService.postDriverAvailability(userId, req))); }
        catch (Exception e) { return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage())); }
    }

    /** GET /api/schedules/mine?userId=... — Get my schedules */
    @GetMapping("/mine")
    public ResponseEntity<ApiResponse<List<Schedule>>> mySchedules(@RequestParam String userId) {
        return ResponseEntity.ok(ApiResponse.ok(scheduleService.getUserSchedules(userId)));
    }

    /** GET /api/schedules/available-drivers?date=2026-03-16 — Find drivers for a date */
    @GetMapping("/available-drivers")
    public ResponseEntity<ApiResponse<List<Schedule>>> availableDrivers(@RequestParam String date) {
        return ResponseEntity.ok(ApiResponse.ok(scheduleService.findAvailableDrivers(LocalDate.parse(date))));
    }

    /** GET /api/schedules/ride-requests?date=2026-03-16 — Find ride requests for a date */
    @GetMapping("/ride-requests")
    public ResponseEntity<ApiResponse<List<Schedule>>> rideRequests(@RequestParam String date) {
        return ResponseEntity.ok(ApiResponse.ok(scheduleService.findUnmatchedRequests(LocalDate.parse(date))));
    }

    /** POST /api/schedules/match — Match a rider with a driver */
    @PostMapping("/match")
    public ResponseEntity<ApiResponse<String>> match(@RequestParam String riderScheduleId, @RequestParam String driverScheduleId) {
        try {
            scheduleService.matchSchedules(riderScheduleId, driverScheduleId);
            return ResponseEntity.ok(ApiResponse.ok("Matched!", "success"));
        } catch (Exception e) { return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage())); }
    }

    /** GET /api/schedules/{scheduleId}/matches — Find matching opposite-type schedules */
    @GetMapping("/{scheduleId}/matches")
    public ResponseEntity<ApiResponse<List<ScheduleMatchResponse>>> findMatches(@PathVariable String scheduleId) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(scheduleService.findMatches(scheduleId)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /** POST /api/schedules/{scheduleId}/accept-match?matchId=... — Accept a match, linking both schedules */
    @PostMapping("/{scheduleId}/accept-match")
    public ResponseEntity<ApiResponse<String>> acceptMatch(
            @PathVariable String scheduleId, @RequestParam String matchId) {
        try {
            scheduleService.acceptMatch(scheduleId, matchId);
            return ResponseEntity.ok(ApiResponse.ok("Match accepted", "success"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /** DELETE /api/schedules/{id}?userId=... — Delete a schedule */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable String id, @RequestParam String userId) {
        try {
            scheduleService.deleteSchedule(id, userId);
            return ResponseEntity.ok(ApiResponse.ok("Deleted", "success"));
        } catch (Exception e) { return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage())); }
    }
}
