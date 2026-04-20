package com.project.uniride.repository;

import com.project.uniride.model.Ride;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;
import java.util.Optional;

public interface RideRepository extends MongoRepository<Ride, String> {
    List<Ride> findByRiderIdOrderByRequestedAtDesc(String riderId);
    List<Ride> findByDriverIdOrderByRequestedAtDesc(String driverId);

    @Query("{ 'riderId': ?0, 'status': { $in: ['REQUESTED','ACCEPTED','DRIVER_ARRIVED','IN_PROGRESS'] } }")
    Optional<Ride> findActiveRideByRiderId(String riderId);

    @Query("{ 'driverId': ?0, 'status': { $in: ['ACCEPTED','DRIVER_ARRIVED','IN_PROGRESS'] } }")
    Optional<Ride> findActiveRideByDriverId(String driverId);

    @Query("{ 'status': 'REQUESTED', 'driverId': null }")
    List<Ride> findPendingRequests();
}
