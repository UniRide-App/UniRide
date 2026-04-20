package com.project.uniride.Implementation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//Author: Hannah Lowery

@Service
public class StudentDriverService implements UserDetailsService {
    private final Map<String, StudentDriver> drivers = new HashMap<>();

    public StudentDriverService(PasswordEncoder passwordEncoder) {
        Car car = new Car("Toyota", "Camry", "Blue", "ABC123", 2020);
        StudentDriver driver = new StudentDriver(
            "driver1",
            "Mike",
            "Johnson",
            "driver@lsu.edu",
            "LSU",
            passwordEncoder.encode("password456"),
            true,
            car
        );
        drivers.put(driver.getUsername(), driver);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        StudentDriver driver = drivers.get(username);
        if (driver == null) {
            throw new UsernameNotFoundException("Driver not found: " + username);
        }
        return driver;
    }

    public void registerDriver(StudentDriver driver) {
        drivers.put(driver.getUsername(), driver);
    }

    public StudentDriver findByUsername(String username) {
        return drivers.get(username);
    }
}