package com.project.uniride.Implementation;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("UniRide - Verify Your College Email");
        message.setText("Thank you for registering with UniRide!\n\n"
            + "Please click the link below to verify your college email:\n"
            + "http://localhost:8080/verify?token=" + token + "\n\n"
            + "If you did not register for UniRide, please ignore this email.");
        mailSender.send(message);
    }
}
