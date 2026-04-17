package com.project.uniride.Implementation;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StudentService {
    private final Map<String, StudentPassenger> students = new HashMap<>();

    public StudentService(PasswordEncoder passwordEncoder){
        StudentPassenger student = new StudentPassenger(
                1L,
                "student1",
                passwordEncoder.encode("password123"),
                "student@1lsu.edu",
                true
        );

        System.out.println("Encoded password: " + student.getPassword());
        students.put(student.getUsername(), student);
    }
    public StudentPassenger findByUsername(String username){
        return students.get(username);
    }
}

