package com.example.demo2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disable CSRF
                .authorizeHttpRequests()
                .requestMatchers("/auth/**").permitAll() // Allow all requests to /auth routes
                .anyRequest().authenticated() // Require authentication for other routes
                .and()
                .httpBasic(); // Basic Authentication for simplicity

        return http.build();
    }
}
