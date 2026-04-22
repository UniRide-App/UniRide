package com.project.uniride.Implementation;
//class type: who they are
//Author: Jermiah Mckeey



import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class StudentPassenger extends Person implements UserDetails {
    private String username;

    public StudentPassenger() {
        super(null, null, null, null, null);
    }

    public StudentPassenger(String username, String firstName, String lastName,
                            String email, String school, String password) {
        super(firstName, lastName, email, school, password);
        this.username = username;
    }

    // Getters
    public String getUsername() { return username; }

    // Setters
    public void setUsername(String username) { this.username = username; }

    // Spring Security methods
    @Override
    public String getPassword() { return super.getPassword(); }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return List.of(); }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return isVerified; }
}