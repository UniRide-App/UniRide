package com.project.uniride.Implementation;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.uniride.Entities.StudentPassengerEntity;
import com.project.uniride.Repositories.StudentPassengerRepository;

@Service
public class StudentPassengerService implements UserDetailsService {
    private final StudentPassengerRepository repository;
    private final PasswordEncoder passwordEncoder;

    public StudentPassengerService(StudentPassengerRepository repository,
                                   PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        StudentPassengerEntity student = repository.findByEmail(username);
        if (student == null) {
            throw new UsernameNotFoundException("Student not found: " + username);
        }
        // Check if verified before allowing login
        if (!student.getIsVerified()) {
            throw new UsernameNotFoundException("Student not verified: " + username);
        }
        return new StudentPassenger(
            student.getEmail(),
            student.getFirstName(),
            student.getLastName(),
            student.getEmail(),
            student.getSchool(),
            student.getpassword()
        );
    }

    public StudentPassengerEntity findByEmail(String email) {
        return repository.findByEmail(email);
    }
}