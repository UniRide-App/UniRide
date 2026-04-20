package com.project.uniride.dto;

import com.project.uniride.model.GeoLocation;
import com.project.uniride.model.Vehicle;

public class DTOs {

    // ─── Auth ───
    public static class RegisterRequest {
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNumber;
        private String password;

        public String getFirstName() { return firstName; }
        public void setFirstName(String f) { this.firstName = f; }
        public String getLastName() { return lastName; }
        public void setLastName(String l) { this.lastName = l; }
        public String getEmail() { return email; }
        public void setEmail(String e) { this.email = e; }
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String p) { this.phoneNumber = p; }
        public String getPassword() { return password; }
        public void setPassword(String p) { this.password = p; }
    }

    public static class OtpVerifyRequest {
        private String email;
        private String code;
        public String getEmail() { return email; }
        public void setEmail(String e) { this.email = e; }
        public String getCode() { return code; }
        public void setCode(String c) { this.code = c; }
    }

    public static class LoginRequest {
        private String email;
        private String password;
        public String getEmail() { return email; }
        public void setEmail(String e) { this.email = e; }
        public String getPassword() { return password; }
        public void setPassword(String p) { this.password = p; }
    }

    // ─── User ───
    public static class AccountTypeRequest {
        private String accountType; // "PASSENGER" or "DRIVER"
        private Vehicle vehicle;
        public String getAccountType() { return accountType; }
        public void setAccountType(String a) { this.accountType = a; }
        public Vehicle getVehicle() { return vehicle; }
        public void setVehicle(Vehicle v) { this.vehicle = v; }
    }

    public static class LocationUpdate {
        private double latitude;
        private double longitude;
        public double getLatitude() { return latitude; }
        public void setLatitude(double l) { this.latitude = l; }
        public double getLongitude() { return longitude; }
        public void setLongitude(double l) { this.longitude = l; }
    }

    // ─── Ride ───
    public static class RideRequest {
        private String pickupAddress;
        private GeoLocation pickupLocation;
        private String destinationAddress;
        private GeoLocation destinationLocation;
        public String getPickupAddress() { return pickupAddress; }
        public void setPickupAddress(String p) { this.pickupAddress = p; }
        public GeoLocation getPickupLocation() { return pickupLocation; }
        public void setPickupLocation(GeoLocation p) { this.pickupLocation = p; }
        public String getDestinationAddress() { return destinationAddress; }
        public void setDestinationAddress(String d) { this.destinationAddress = d; }
        public GeoLocation getDestinationLocation() { return destinationLocation; }
        public void setDestinationLocation(GeoLocation d) { this.destinationLocation = d; }
    }

    public static class RatingRequest {
        private int stars;
        private String review;
        public int getStars() { return stars; }
        public void setStars(int s) { this.stars = s; }
        public String getReview() { return review; }
        public void setReview(String r) { this.review = r; }
    }

    // ─── Schedule ───
    public static class ScheduleRequest {
        private String date; // "2026-03-16"
        private String arrivalTime; // "08:00"
        private String leavingFrom;
        private String goingTo;
        private String dayOfWeek;
        private boolean recurring;
        public String getDate() { return date; }
        public void setDate(String d) { this.date = d; }
        public String getArrivalTime() { return arrivalTime; }
        public void setArrivalTime(String a) { this.arrivalTime = a; }
        public String getLeavingFrom() { return leavingFrom; }
        public void setLeavingFrom(String l) { this.leavingFrom = l; }
        public String getGoingTo() { return goingTo; }
        public void setGoingTo(String g) { this.goingTo = g; }
        public String getDayOfWeek() { return dayOfWeek; }
        public void setDayOfWeek(String d) { this.dayOfWeek = d; }
        public boolean isRecurring() { return recurring; }
        public void setRecurring(boolean r) { this.recurring = r; }
    }

    // ─── Nearby Driver Response ───
    public static class NearbyDriverResponse {
        private String driverId;
        private String displayName;
        private double rating;
        private Vehicle vehicle;
        private double distanceMiles;
        private int estimatedMinutes;

        public String getDriverId() { return driverId; }
        public void setDriverId(String d) { this.driverId = d; }
        public String getDisplayName() { return displayName; }
        public void setDisplayName(String d) { this.displayName = d; }
        public double getRating() { return rating; }
        public void setRating(double r) { this.rating = r; }
        public Vehicle getVehicle() { return vehicle; }
        public void setVehicle(Vehicle v) { this.vehicle = v; }
        public double getDistanceMiles() { return distanceMiles; }
        public void setDistanceMiles(double d) { this.distanceMiles = d; }
        public int getEstimatedMinutes() { return estimatedMinutes; }
        public void setEstimatedMinutes(int e) { this.estimatedMinutes = e; }
    }

    // ─── Schedule Match Response ───
    public static class ScheduleMatchResponse {
        private String scheduleId;
        private String userName;
        private String arrivalTime;   // "HH:mm"
        private String leavingFrom;
        private String goingTo;
        private long timeDiffMinutes; // absolute diff vs. the queried schedule

        public String getScheduleId() { return scheduleId; }
        public void setScheduleId(String s) { this.scheduleId = s; }
        public String getUserName() { return userName; }
        public void setUserName(String u) { this.userName = u; }
        public String getArrivalTime() { return arrivalTime; }
        public void setArrivalTime(String a) { this.arrivalTime = a; }
        public String getLeavingFrom() { return leavingFrom; }
        public void setLeavingFrom(String l) { this.leavingFrom = l; }
        public String getGoingTo() { return goingTo; }
        public void setGoingTo(String g) { this.goingTo = g; }
        public long getTimeDiffMinutes() { return timeDiffMinutes; }
        public void setTimeDiffMinutes(long t) { this.timeDiffMinutes = t; }
    }

    // ─── API Response Wrapper ───
    public static class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;

        public ApiResponse(boolean success, String message, T data) {
            this.success = success; this.message = message; this.data = data;
        }
        public static <T> ApiResponse<T> ok(T data) { return new ApiResponse<>(true, "Success", data); }
        public static <T> ApiResponse<T> ok(String msg, T data) { return new ApiResponse<>(true, msg, data); }
        public static <T> ApiResponse<T> error(String msg) { return new ApiResponse<>(false, msg, null); }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public T getData() { return data; }
    }
}
