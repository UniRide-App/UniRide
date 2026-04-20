package com.project.uniride.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.getApplicationDefault())
                        .build();
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase Admin SDK initialized");
            }
        } catch (IOException e) {
            System.err.println("Firebase init skipped — no credentials found.");
            System.err.println("Set GOOGLE_APPLICATION_CREDENTIALS env var or add service account JSON.");
            System.err.println("App will run without Firebase auth verification.");
        }
    }
}
