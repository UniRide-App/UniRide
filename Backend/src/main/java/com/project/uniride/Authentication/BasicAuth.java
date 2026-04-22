package com.project.uniride.Authentication;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.project.uniride.Implementation.StudentDriverService;
import com.project.uniride.Implementation.StudentPassengerService;

@Configuration
@EnableWebSecurity
public class BasicAuth {
    private final StudentPassengerService studentPassengerService;
    private final StudentDriverService studentDriverService;

    public BasicAuth(StudentPassengerService studentPassengerService,
                     StudentDriverService studentDriverService) {
        this.studentPassengerService = studentPassengerService;
        this.studentDriverService = studentDriverService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/studentpassengers/register").permitAll()
                .requestMatchers("/studentdrivers/register").permitAll()
                .requestMatchers("/verify").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}