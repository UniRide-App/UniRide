package com.project.uniride.Implementation;

//defines the Driver in the UniRide app
//class type: who they are
//Author: Hannah Lowery


import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class StudentDriver extends Person implements UserDetails {
    private String username;
    private Car car;

    public StudentDriver(String username, String firstName, String lastName,
                         String email, String school, String password, Car car) {
        super(firstName, lastName, email, school, password);
        this.username = username;
        
        this.car = car;
    }
     public StudentDriver() {
        super(null, null, null, null, null);
    }

    // Getters
    public Car getCar() {
        return car;
    }

    // Setters
    public void setCar(Car car) {
        this.car = car;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Spring Security required methods
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isVerified;
    }
}
