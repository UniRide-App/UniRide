package com.project.uniride.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    private String id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String firebaseUid;

    private String firstName;
    private String lastName;
    private String phoneNumber;

    public enum AccountType { PASSENGER, DRIVER }

    @Enumerated(EnumType.STRING)
    private AccountType accountType = AccountType.PASSENGER;

    private boolean isOnline;

    @Embedded
    private Vehicle vehicle;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "latitude",  column = @Column(name = "current_lat")),
        @AttributeOverride(name = "longitude", column = @Column(name = "current_lng"))
    })
    private GeoLocation currentLocation;

    private double averageRating = 5.0;
    private int totalRides;
    private int totalRatingsReceived;
    private double ratingSum;

    @ElementCollection
    @CollectionTable(name = "user_payment_methods", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "payment_method")
    private List<String> paymentMethods = new ArrayList<>();

    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    protected void prePersist() {
        if (id == null) id = UUID.randomUUID().toString();
    }

    // ─── Getters & Setters ───
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirebaseUid() { return firebaseUid; }
    public void setFirebaseUid(String firebaseUid) { this.firebaseUid = firebaseUid; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getDisplayName() { return firstName + " " + lastName; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }
    public boolean isOnline() { return isOnline; }
    public void setOnline(boolean online) { isOnline = online; }
    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
    public GeoLocation getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(GeoLocation currentLocation) { this.currentLocation = currentLocation; }
    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    public int getTotalRides() { return totalRides; }
    public void setTotalRides(int totalRides) { this.totalRides = totalRides; }
    public int getTotalRatingsReceived() { return totalRatingsReceived; }
    public void setTotalRatingsReceived(int r) { this.totalRatingsReceived = r; }
    public double getRatingSum() { return ratingSum; }
    public void setRatingSum(double ratingSum) { this.ratingSum = ratingSum; }
    public List<String> getPaymentMethods() { return paymentMethods; }
    public void setPaymentMethods(List<String> p) { this.paymentMethods = p; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public void addRating(int stars) {
        this.ratingSum += stars;
        this.totalRatingsReceived++;
        this.averageRating = this.ratingSum / this.totalRatingsReceived;
    }
}
