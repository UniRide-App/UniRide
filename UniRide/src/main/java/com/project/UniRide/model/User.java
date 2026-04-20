package com.project.uniride.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    private String firebaseUid;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String passwordHash; // handled by Firebase, stored for reference
    private boolean emailVerified;

    // Role
    public enum AccountType { PASSENGER, DRIVER }
    private AccountType accountType = AccountType.PASSENGER;
    private boolean isOnline;

    // Driver-specific
    private Vehicle vehicle;

    // Location
    private GeoLocation currentLocation;

    // Rating
    private double averageRating = 5.0;
    private int totalRides;
    private int totalRatingsReceived;
    private double ratingSum;

    // Payment
    private List<String> paymentMethods = new ArrayList<>();

    // OTP
    private String otpCode;
    private Instant otpExpiresAt;

    private Instant createdAt;
    private Instant updatedAt;

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
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public boolean isEmailVerified() { return emailVerified; }
    public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }
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
    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }
    public Instant getOtpExpiresAt() { return otpExpiresAt; }
    public void setOtpExpiresAt(Instant otpExpiresAt) { this.otpExpiresAt = otpExpiresAt; }
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
