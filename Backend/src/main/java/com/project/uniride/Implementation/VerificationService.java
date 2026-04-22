package com.project.uniride.Implementation;

import org.springframework.stereotype.Service;
import java.util.UUID;
import com.project.uniride.Entities.StudentPassengerEntity;
import com.project.uniride.Entities.StudentDriverEntity;
import com.project.uniride.Repositories.StudentPassengerRepository;
import com.project.uniride.Repositories.StudentDriverRepository;

@Service
public class VerificationService {
    private final StudentPassengerRepository passengerRepository;
    private final StudentDriverRepository driverRepository;
    private final EmailService emailService;

    public VerificationService(StudentPassengerRepository passengerRepository,
                                StudentDriverRepository driverRepository,
                                EmailService emailService) {
        this.passengerRepository = passengerRepository;
        this.driverRepository = driverRepository;
        this.emailService = emailService;
    }

    public void sendPassengerVerification(StudentPassengerEntity passenger) {
        String token = UUID.randomUUID().toString();
        passenger.setVerificationToken(token);
        passenger.setIsVerified(false);
        passengerRepository.save(passenger);
        emailService.sendVerificationEmail(passenger.getEmail(), token);
    }

    public void sendDriverVerification(StudentDriverEntity driver) {
        String token = UUID.randomUUID().toString();
        driver.setVerificationToken(token);
        driver.setIsVerified(false);
        driverRepository.save(driver);
        emailService.sendVerificationEmail(driver.getEmail(), token);
    }

    public boolean verifyPassenger(String token) {
        StudentPassengerEntity passenger = passengerRepository.findByVerificationToken(token);
        if (passenger == null) return false;
        passenger.setIsVerified(true);
        passenger.setVerificationToken(null);
        passengerRepository.save(passenger);
        return true;
    }

    public boolean verifyDriver(String token) {
        StudentDriverEntity driver = driverRepository.findByVerificationToken(token);
        if (driver == null) return false;
        driver.setIsVerified(true);
        driver.setVerificationToken(null);
        driverRepository.save(driver);
        return true;
    }
}
