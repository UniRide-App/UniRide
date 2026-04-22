package com.project.uniride.Implementation;

//Author: Hannah Lowery


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.uniride.Entities.StudentDriverEntity;
import com.project.uniride.Repositories.StudentDriverRepository;

@Service
public class StudentDriverService implements UserDetailsService {
    private final StudentDriverRepository repository;
    private final PasswordEncoder passwordEncoder;

    public StudentDriverService(StudentDriverRepository repository,
                                PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        StudentDriverEntity driver = repository.findByEmail(username);
        if (driver == null) {
            throw new UsernameNotFoundException("Driver not found: " + username);
        }
        // Check if verified before allowing login
        if (!driver.getIsVerified()) {
            throw new UsernameNotFoundException("Driver not verified: " + username);
        }
        return new StudentDriver(
            driver.getEmail(),
            driver.getFirstName(),
            driver.getLastName(),
            driver.getEmail(),
            driver.getSchool(),
            driver.getpassword(),
            null
        );
    }

    public StudentDriverEntity findByEmail(String email) {
        return repository.findByEmail(email);
    }
}

