package com.project.uniride.service;

import com.project.uniride.dto.DTOs.*;
import com.project.uniride.model.*;
import com.project.uniride.model.Schedule.ScheduleType;
import com.project.uniride.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    private final ScheduleRepository schedRepo;
    private final UserService userService;

    public ScheduleService(ScheduleRepository schedRepo, UserService userService) {
        this.schedRepo = schedRepo; this.userService = userService;
    }

    /** Rider posts a scheduled ride request */
    public Schedule postRideSchedule(String userId, ScheduleRequest req) {
        User user = userService.findById(userId);
        Schedule sched = buildSchedule(user, req, ScheduleType.RIDE_REQUEST);
        return schedRepo.save(sched);
    }

    /** Driver posts their availability */
    public Schedule postDriverAvailability(String userId, ScheduleRequest req) {
        User user = userService.findById(userId);
        if (user.getAccountType() != User.AccountType.DRIVER)
            throw new IllegalStateException("Only drivers can post availability");
        Schedule sched = buildSchedule(user, req, ScheduleType.DRIVER_AVAILABILITY);
        return schedRepo.save(sched);
    }

    /** Get all schedules for a user */
    public List<Schedule> getUserSchedules(String userId) {
        return schedRepo.findByUserIdOrderByDateDesc(userId);
    }

    /** Find available drivers for a specific date */
    public List<Schedule> findAvailableDrivers(LocalDate date) {
        return schedRepo.findByTypeAndMatchedFalseAndDate(ScheduleType.DRIVER_AVAILABILITY, date);
    }

    /** Find unmatched ride requests for a specific date */
    public List<Schedule> findUnmatchedRequests(LocalDate date) {
        return schedRepo.findByTypeAndMatchedFalseAndDate(ScheduleType.RIDE_REQUEST, date);
    }

    /** Match a rider's schedule with a driver's availability */
    public void matchSchedules(String riderScheduleId, String driverScheduleId) {
        Schedule riderSched = schedRepo.findById(riderScheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Rider schedule not found"));
        Schedule driverSched = schedRepo.findById(driverScheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Driver schedule not found"));

        riderSched.setMatched(true);
        riderSched.setMatchedWithUserId(driverSched.getUserId());
        driverSched.setMatched(true);
        driverSched.setMatchedWithUserId(riderSched.getUserId());

        schedRepo.save(riderSched);
        schedRepo.save(driverSched);
    }

    /** Find schedules of the opposite type that match on date, time (±30 min), and destination */
    public List<ScheduleMatchResponse> findMatches(String scheduleId) {
        Schedule target = schedRepo.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));

        if (target.getDate() == null || target.getArrivalTime() == null)
            throw new IllegalArgumentException("Schedule must have a date and arrival time to find matches");

        ScheduleType oppositeType = target.getType() == ScheduleType.RIDE_REQUEST
                ? ScheduleType.DRIVER_AVAILABILITY
                : ScheduleType.RIDE_REQUEST;

        return schedRepo.findByDateAndType(target.getDate(), oppositeType).stream()
                .filter(s -> !s.isMatched())
                .filter(s -> s.getArrivalTime() != null
                        && Math.abs(ChronoUnit.MINUTES.between(target.getArrivalTime(), s.getArrivalTime())) <= 30)
                .filter(s -> destinationMatches(target.getGoingTo(), s.getGoingTo()))
                .map(s -> {
                    long diff = Math.abs(ChronoUnit.MINUTES.between(target.getArrivalTime(), s.getArrivalTime()));
                    ScheduleMatchResponse r = new ScheduleMatchResponse();
                    r.setScheduleId(s.getId());
                    r.setUserName(s.getUserName());
                    r.setArrivalTime(s.getArrivalTime().toString());
                    r.setLeavingFrom(s.getLeavingFrom());
                    r.setGoingTo(s.getGoingTo());
                    r.setTimeDiffMinutes(diff);
                    return r;
                })
                .sorted((a, b) -> Long.compare(a.getTimeDiffMinutes(), b.getTimeDiffMinutes()))
                .collect(Collectors.toList());
    }

    /** Mark both schedules as matched and link them to each other */
    public void acceptMatch(String scheduleId, String matchId) {
        Schedule s1 = schedRepo.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));
        Schedule s2 = schedRepo.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match schedule not found"));
        s1.setMatched(true);
        s1.setMatchedWithUserId(s2.getUserId());
        s2.setMatched(true);
        s2.setMatchedWithUserId(s1.getUserId());
        schedRepo.save(s1);
        schedRepo.save(s2);
    }

    private boolean destinationMatches(String a, String b) {
        if (a == null || b == null) return false;
        String na = normalize(a), nb = normalize(b);
        return na.equals(nb) || na.contains(nb) || nb.contains(na) || firstWord(na).equals(firstWord(nb));
    }

    private String normalize(String s) {
        return s.toLowerCase().replaceAll("[^a-z0-9 ]", "").trim().replaceAll("\\s+", " ");
    }

    private String firstWord(String s) {
        int sp = s.indexOf(' ');
        return sp > 0 ? s.substring(0, sp) : s;
    }

    public void deleteSchedule(String scheduleId, String userId) {
        Schedule sched = schedRepo.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));
        if (!sched.getUserId().equals(userId))
            throw new IllegalArgumentException("Not your schedule");
        schedRepo.delete(sched);
    }

    private Schedule buildSchedule(User user, ScheduleRequest req, ScheduleType type) {
        Schedule s = new Schedule();
        s.setUserId(user.getId());
        s.setUserName(user.getDisplayName());
        s.setType(type);
        if (req.getDate() != null) s.setDate(LocalDate.parse(req.getDate()));
        if (req.getArrivalTime() != null) s.setArrivalTime(LocalTime.parse(req.getArrivalTime()));
        s.setLeavingFrom(req.getLeavingFrom());
        s.setGoingTo(req.getGoingTo());
        s.setDayOfWeek(req.getDayOfWeek());
        s.setRecurring(req.isRecurring());
        s.setMatched(false);
        s.setCreatedAt(Instant.now());
        return s;
    }
}
