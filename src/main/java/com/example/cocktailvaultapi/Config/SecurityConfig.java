package com.example.cocktailvaultapi.Config;

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
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/cocktails/**").permitAll()  // Allow public access to cocktail endpoints
                        .requestMatchers("/api/ingredients/**").permitAll() // Allow public access to ingredient endpoints
                        .anyRequest().authenticated()  // Require authentication for any other request
                );

        return http.build();
    }
}
