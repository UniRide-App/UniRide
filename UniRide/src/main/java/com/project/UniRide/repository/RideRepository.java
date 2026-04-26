package com.project.uniride.repository;

import com.project.uniride.model.Ride;
import com.project.uniride.model.Ride.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RideRepository extends JpaRepository<Ride, String> {
    List<Ride> findByRiderIdOrderByRequestedAtDesc(String riderId);
    List<Ride> findByDriverIdOrderByRequestedAtDesc(String driverId);
    Optional<Ride> findFirstByRiderIdAndStatusIn(String riderId, Collection<RideStatus> statuses);
    Optional<Ride> findFirstByDriverIdAndStatusIn(String driverId, Collection<RideStatus> statuses);
    List<Ride> findByStatusAndDriverIdIsNull(RideStatus status);
}
