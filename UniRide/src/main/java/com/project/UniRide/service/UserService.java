package com.project.uniride.service;

import com.project.uniride.dto.DTOs.*;
import com.project.uniride.model.*;
import com.project.uniride.model.User.AccountType;
import com.project.uniride.repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final JavaMailSender mailSender;

    public UserService(UserRepository userRepo, JavaMailSender mailSender) {
        this.userRepo = userRepo;
        this.mailSender = mailSender;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Password hashing failed", e);
        }
    }

    private boolean verifyPassword(String plaintext, String storedHash) {
        return hashPassword(plaintext).equals(storedHash);
    }

    private void sendOtpEmail(String toEmail, String otp) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(toEmail);
            msg.setSubject("UniRide Verification Code");
            msg.setText("Your UniRide verification code is: " + otp + ". This code expires in 10 minutes.");
            mailSender.send(msg);
            System.out.println("[UniRide] OTP email sent to " + toEmail);
        } catch (Exception e) {
            // Email unavailable — print OTP to console so dev flow still works
            System.out.println("[UniRide] ⚠ Email send failed (" + e.getMessage() + ")");
            System.out.println("[UniRide] OTP for " + toEmail + " → " + otp);
        }
    }

    public User register(RegisterRequest req) {
        if (!req.getEmail().endsWith("@lsu.edu"))
            throw new IllegalArgumentException("Only @lsu.edu emails allowed");
        if (userRepo.existsByEmail(req.getEmail()))
            throw new IllegalArgumentException("Email already registered");

        User user = new User();
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setEmail(req.getEmail());
        user.setPhoneNumber(req.getPhoneNumber());
        if (req.getPassword() != null && !req.getPassword().isEmpty())
            user.setPasswordHash(hashPassword(req.getPassword()));
        user.setEmailVerified(false);
        user.setAccountType(AccountType.PASSENGER);
        user.setOnline(false);
        user.setAverageRating(5.0);
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        // Generate 4-digit OTP
        String otp = String.format("%04d", new Random().nextInt(10000));
        user.setOtpCode(otp);
        user.setOtpExpiresAt(Instant.now().plusSeconds(600)); // 10 min

        sendOtpEmail(req.getEmail(), otp);

        return userRepo.save(user);
    }

    public User verifyOtp(OtpVerifyRequest req) {
        User user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getOtpCode() == null || !user.getOtpCode().equals(req.getCode()))
            throw new IllegalArgumentException("Invalid OTP code");
        if (Instant.now().isAfter(user.getOtpExpiresAt()))
            throw new IllegalArgumentException("OTP expired. Please request a new one.");

        user.setEmailVerified(true);
        user.setOtpCode(null);
        user.setOtpExpiresAt(null);
        user.setUpdatedAt(Instant.now());
        return userRepo.save(user);
    }

    public User resendOtp(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        String otp = String.format("%04d", new Random().nextInt(10000));
        user.setOtpCode(otp);
        user.setOtpExpiresAt(Instant.now().plusSeconds(600));
        sendOtpEmail(email, otp);
        return userRepo.save(user);
    }

    public User login(LoginRequest req) {
        User user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("No account found with that email"));
        if (!user.isEmailVerified())
            throw new IllegalArgumentException("Email not verified. Please check your inbox for an OTP.");
        if (user.getPasswordHash() == null || !verifyPassword(req.getPassword(), user.getPasswordHash()))
            throw new IllegalArgumentException("Incorrect password");
        return user;
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
        return userRepo.save(user);
    }

    public List<NearbyDriverResponse> findNearbyDrivers(GeoLocation riderLoc, int max) {
        return userRepo.findAvailableDrivers().stream()
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

    public User save(User user) { return userRepo.save(user); }
}
