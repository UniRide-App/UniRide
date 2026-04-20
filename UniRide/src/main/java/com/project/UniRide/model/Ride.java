package com.project.uniride.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "rides")
public class Ride {

    @Id
    private String id;

    private String riderId;
    private String driverId;
    private String riderName;
    private String driverName;

    private String pickupAddress;
    private GeoLocation pickupLocation;
    private String destinationAddress;
    private GeoLocation destinationLocation;

    public enum RideStatus { REQUESTED, ACCEPTED, DRIVER_ARRIVED, IN_PROGRESS, COMPLETED, CANCELLED }
    private RideStatus status;

    // Pricing
    private double price;
    private double driverEarnings;

    // Timestamps
    private Instant requestedAt;
    private Instant acceptedAt;
    private Instant driverArrivedAt;
    private Instant startedAt;
    private Instant completedAt;
    private Instant cancelledAt;
    private String cancelledBy;
    private String cancellationReason;

    // Distance & time
    private double estimatedDistanceMiles;
    private int estimatedDurationMinutes;

    // Ratings
    private Integer riderRating;
    private Integer driverRating;

    // ─── Getters & Setters ───
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRiderId() { return riderId; }
    public void setRiderId(String riderId) { this.riderId = riderId; }
    public String getDriverId() { return driverId; }
    public void setDriverId(String driverId) { this.driverId = driverId; }
    public String getRiderName() { return riderName; }
    public void setRiderName(String riderName) { this.riderName = riderName; }
    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }
    public String getPickupAddress() { return pickupAddress; }
    public void setPickupAddress(String pa) { this.pickupAddress = pa; }
    public GeoLocation getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(GeoLocation pl) { this.pickupLocation = pl; }
    public String getDestinationAddress() { return destinationAddress; }
    public void setDestinationAddress(String da) { this.destinationAddress = da; }
    public GeoLocation getDestinationLocation() { return destinationLocation; }
    public void setDestinationLocation(GeoLocation dl) { this.destinationLocation = dl; }
    public RideStatus getStatus() { return status; }
    public void setStatus(RideStatus status) { this.status = status; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public double getDriverEarnings() { return driverEarnings; }
    public void setDriverEarnings(double de) { this.driverEarnings = de; }
    public Instant getRequestedAt() { return requestedAt; }
    public void setRequestedAt(Instant r) { this.requestedAt = r; }
    public Instant getAcceptedAt() { return acceptedAt; }
    public void setAcceptedAt(Instant a) { this.acceptedAt = a; }
    public Instant getDriverArrivedAt() { return driverArrivedAt; }
    public void setDriverArrivedAt(Instant d) { this.driverArrivedAt = d; }
    public Instant getStartedAt() { return startedAt; }
    public void setStartedAt(Instant s) { this.startedAt = s; }
    public Instant getCompletedAt() { return completedAt; }
    public void setCompletedAt(Instant c) { this.completedAt = c; }
    public Instant getCancelledAt() { return cancelledAt; }
    public void setCancelledAt(Instant c) { this.cancelledAt = c; }
    public String getCancelledBy() { return cancelledBy; }
    public void setCancelledBy(String c) { this.cancelledBy = c; }
    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String c) { this.cancellationReason = c; }
    public double getEstimatedDistanceMiles() { return estimatedDistanceMiles; }
    public void setEstimatedDistanceMiles(double e) { this.estimatedDistanceMiles = e; }
    public int getEstimatedDurationMinutes() { return estimatedDurationMinutes; }
    public void setEstimatedDurationMinutes(int e) { this.estimatedDurationMinutes = e; }
    public Integer getRiderRating() { return riderRating; }
    public void setRiderRating(Integer r) { this.riderRating = r; }
    public Integer getDriverRating() { return driverRating; }
    public void setDriverRating(Integer d) { this.driverRating = d; }

    /** Calculate price based on distance: $3 base + $1.50/mile */
    public static double calculatePrice(double distanceMiles) {
        return Math.round((3.0 + distanceMiles * 1.50) * 100.0) / 100.0;
    }
}
