package com.project.uniride.service;

import com.project.uniride.dto.DTOs.*;
import com.project.uniride.model.*;
import com.project.uniride.model.User.AccountType;
import com.project.uniride.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final SimpMessagingTemplate messaging;

    public UserService(UserRepository userRepo, SimpMessagingTemplate messaging) {
        this.userRepo = userRepo;
        this.messaging = messaging;
    }

    public User registerWithFirebase(String firebaseUid, String email, RegisterRequest req) {
        if (!email.endsWith("@lsu.edu"))
            throw new IllegalArgumentException("Only @lsu.edu emails are allowed");

        // If the email exists but under a different Firebase UID, the old Firebase account
        // was deleted and recreated — reuse and update the existing profile.
        Optional<User> existing = userRepo.findByEmail(email);
        if (existing.isPresent()) {
            User user = existing.get();
            if (user.getFirebaseUid().equals(firebaseUid))
                throw new IllegalArgumentException("Email already registered");
            user.setFirebaseUid(firebaseUid);
            user.setFirstName(req.getFirstName());
            user.setLastName(req.getLastName());
            user.setPhoneNumber(req.getPhoneNumber());
            user.setUpdatedAt(Instant.now());
            return userRepo.save(user);
        }

        User user = new User();
        user.setFirebaseUid(firebaseUid);
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setEmail(email);
        user.setPhoneNumber(req.getPhoneNumber());
        user.setAccountType(AccountType.PASSENGER);
        user.setOnline(false);
        user.setAverageRating(5.0);
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        return userRepo.save(user);
    }

    public User loginWithFirebase(String firebaseUid) {
        return userRepo.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new IllegalArgumentException("No profile found — please register first"));
    }

    public User switchAccountType(String userId, AccountTypeRequest req) {
        User user = findById(userId);
        AccountType type = AccountType.valueOf(req.getAccountType().toUpperCase());
        user.setAccountType(type);
        if (type == AccountType.DRIVER && req.getVehicle() != null)
            user.setVehicle(req.getVehicle());
        if (type == AccountType.PASSENGER) user.setOnline(false);
        user.setUpdatedAt(Instant.now());
        return userRepo.save(user);
    }

    public User setOnline(String userId, boolean online) {
        User user = findById(userId);
        if (online && user.getAccountType() != AccountType.DRIVER)
            throw new IllegalStateException("Switch to Driver mode first");
        user.setOnline(online);
        user.setUpdatedAt(Instant.now());
        return userRepo.save(user);
    }

    public User updateLocation(String userId, LocationUpdate req) {
        User user = findById(userId);
        user.setCurrentLocation(new GeoLocation(req.getLatitude(), req.getLongitude()));
        user.setUpdatedAt(Instant.now());
        User saved = userRepo.save(user);
        messaging.convertAndSend("/topic/driver/" + userId + "/location", saved.getCurrentLocation());
        return saved;
    }

    public List<NearbyDriverResponse> findNearbyDrivers(GeoLocation riderLoc, int max) {
        return userRepo.findByAccountTypeAndIsOnlineTrue(AccountType.DRIVER).stream()
                .map(d -> {
                    NearbyDriverResponse r = new NearbyDriverResponse();
                    r.setDriverId(d.getId());
                    r.setDisplayName(d.getDisplayName());
                    r.setRating(d.getAverageRating());
                    r.setVehicle(d.getVehicle());
                    double dist = riderLoc.distanceTo(d.getCurrentLocation());
                    r.setDistanceMiles(Math.round(dist * 100.0) / 100.0);
                    r.setEstimatedMinutes((int) Math.ceil(dist / 0.5));
                    return r;
                })
                .sorted(Comparator.comparingDouble(NearbyDriverResponse::getDistanceMiles))
                .limit(max).collect(Collectors.toList());
    }

    public User findById(String id) {
        return userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User updateProfile(String userId, String firstName, String lastName, String phoneNumber) {
        User user = findById(userId);
        if (firstName != null && !firstName.isBlank()) user.setFirstName(firstName);
        if (lastName != null && !lastName.isBlank()) user.setLastName(lastName);
        if (phoneNumber != null && !phoneNumber.isBlank()) user.setPhoneNumber(phoneNumber);
        user.setUpdatedAt(java.time.Instant.now());
        return userRepo.save(user);
    }

    public User save(User user) { return userRepo.save(user); }
}
