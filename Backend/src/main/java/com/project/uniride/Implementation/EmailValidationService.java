package com.project.uniride.Implementation;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class EmailValidationService {

    private static final List<String> ACCEPTED_DOMAINS = List.of(
        "@lsu.edu",
        "@tulane.edu",
        "@uno.edu",
        "@selu.edu",
        "@loyno.edu"
    );

    public boolean isCollegeEmail(String email) {
        if (email == null) return false;
        return ACCEPTED_DOMAINS.stream()
            .anyMatch(domain -> email.toLowerCase().endsWith(domain));
    }
}