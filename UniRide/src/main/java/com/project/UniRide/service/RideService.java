package com.project.uniride.service;

import com.project.uniride.dto.DTOs.*;
import com.project.uniride.model.*;
import com.project.uniride.model.Ride.RideStatus;
import com.project.uniride.repository.RideRepository;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class RideService {
    private final RideRepository rideRepo;
    private final UserService userService;

    public RideService(RideRepository rideRepo, UserService userService) {
        this.rideRepo = rideRepo; this.userService = userService;
    }

    public Ride requestRide(String riderId, RideRequest req) {
        User rider = userService.findById(riderId);
        rideRepo.findActiveRideByRiderId(riderId).ifPresent(r -> {
            throw new IllegalStateException("You already have an active ride");
        });

        Ride ride = new Ride();
        ride.setRiderId(riderId);
        ride.setRiderName(rider.getDisplayName());
        ride.setPickupAddress(req.getPickupAddress());
        ride.setPickupLocation(req.getPickupLocation());
        ride.setDestinationAddress(req.getDestinationAddress());
        ride.setDestinationLocation(req.getDestinationLocation());
        ride.setStatus(RideStatus.REQUESTED);
        ride.setRequestedAt(Instant.now());

        if (req.getPickupLocation() != null && req.getDestinationLocation() != null) {
            double dist = req.getPickupLocation().distanceTo(req.getDestinationLocation());
            ride.setEstimatedDistanceMiles(Math.round(dist * 100.0) / 100.0);
            ride.setEstimatedDurationMinutes((int) Math.ceil(dist / 0.5));
            ride.setPrice(Ride.calculatePrice(dist));
            ride.setDriverEarnings(ride.getPrice() * 0.85); // driver gets 85%
        }
        return rideRepo.save(ride);
    }

    public Ride acceptRide(String driverId, String rideId) {
        User driver = userService.findById(driverId);
        Ride ride = findById(rideId);
        if (ride.getStatus() != RideStatus.REQUESTED)
            throw new IllegalStateException("Ride no longer available");
        ride.setDriverId(driverId);
        ride.setDriverName(driver.getDisplayName());
        ride.setStatus(RideStatus.ACCEPTED);
        ride.setAcceptedAt(Instant.now());
        return rideRepo.save(ride);
    }

    public Ride driverArrived(String driverId, String rideId) {
        Ride ride = findById(rideId);
        if (!driverId.equals(ride.getDriverId())) throw new IllegalArgumentException("Not your ride");
        ride.setStatus(RideStatus.DRIVER_ARRIVED);
        ride.setDriverArrivedAt(Instant.now());
        return rideRepo.save(ride);
    }

    public Ride startTrip(String driverId, String rideId) {
        Ride ride = findById(rideId);
        if (!driverId.equals(ride.getDriverId())) throw new IllegalArgumentException("Not your ride");
        ride.setStatus(RideStatus.IN_PROGRESS);
        ride.setStartedAt(Instant.now());
        return rideRepo.save(ride);
    }

    public Ride completeTrip(String driverId, String rideId) {
        Ride ride = findById(rideId);
        if (!driverId.equals(ride.getDriverId())) throw new IllegalArgumentException("Not your ride");
        ride.setStatus(RideStatus.COMPLETED);
        ride.setCompletedAt(Instant.now());

        User rider = userService.findById(ride.getRiderId());
        rider.setTotalRides(rider.getTotalRides() + 1);
        userService.save(rider);
        User driver = userService.findById(driverId);
        driver.setTotalRides(driver.getTotalRides() + 1);
        userService.save(driver);
        return rideRepo.save(ride);
    }

    public Ride cancelRide(String userId, String rideId, String reason) {
        Ride ride = findById(rideId);
        if (ride.getStatus() == RideStatus.DRIVER_ARRIVED && userId.equals(ride.getRiderId()))
            throw new IllegalStateException("Cannot cancel — driver has arrived");
        ride.setStatus(RideStatus.CANCELLED);
        ride.setCancelledAt(Instant.now());
        ride.setCancelledBy(userId);
        ride.setCancellationReason(reason);
        return rideRepo.save(ride);
    }

    public Ride rateRide(String userId, String rideId, RatingRequest req) {
        Ride ride = findById(rideId);
        if (userId.equals(ride.getRiderId())) {
            ride.setDriverRating(req.getStars());
            User driver = userService.findById(ride.getDriverId());
            driver.addRating(req.getStars());
            userService.save(driver);
        } else if (userId.equals(ride.getDriverId())) {
            ride.setRiderRating(req.getStars());
            User rider = userService.findById(ride.getRiderId());
            rider.addRating(req.getStars());
            userService.save(rider);
        }
        return rideRepo.save(ride);
    }

    public List<Ride> getHistory(String userId) {
        List<Ride> rides = rideRepo.findByRiderIdOrderByRequestedAtDesc(userId);
        rides.addAll(rideRepo.findByDriverIdOrderByRequestedAtDesc(userId));
        rides.sort((a, b) -> b.getRequestedAt().compareTo(a.getRequestedAt()));
        return rides;
    }

    public Ride findById(String id) {
        return rideRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Ride not found"));
    }
}
