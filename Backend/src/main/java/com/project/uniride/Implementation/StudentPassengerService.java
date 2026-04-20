package com.project.uniride.Implementation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//Author: Jermiah Mckeey
@Service
public class StudentPassengerService implements UserDetailsService {
    private final Map<String, StudentPassenger> students = new HashMap<>();

    public StudentPassengerService(PasswordEncoder passwordEncoder) {
        StudentPassenger student = new StudentPassenger(
            "student1",
            "John",
            "Doe",
            "student@lsu.edu",
            "LSU",
            passwordEncoder.encode("password123"),
            true
        );
        students.put(student.getUsername(), student);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        StudentPassenger student = students.get(username);
        if (student == null) {
            throw new UsernameNotFoundException("Student not found: " + username);
        }
        return student;
    }

    public void registerStudent(StudentPassenger student) {
        students.put(student.getUsername(), student);
    }

    public StudentPassenger findByUsername(String username) {
        return students.get(username);
    }
}

