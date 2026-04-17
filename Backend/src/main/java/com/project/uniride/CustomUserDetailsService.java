package com.project.uniride;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final StudentService studentService;

    public CustomUserDetailsService(StudentService studentService){
        this.studentService = studentService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        StudentPassenger student = studentService.findByUsername(username);

        if(student == null){
            throw new UsernameNotFoundException("Student not found");
        }
        return new User(
            student.getUsername(),
            student.getPassword(),
            List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
