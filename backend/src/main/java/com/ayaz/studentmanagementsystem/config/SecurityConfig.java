package com.ayaz.studentmanagementsystem.config;

import com.ayaz.studentmanagementsystem.filter.JwtFilter;
import com.ayaz.studentmanagementsystem.service.AuthUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    AuthUserDetails authUserDetails;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(customizer -> customizer.disable());
        httpSecurity.authorizeHttpRequests(authorize ->authorize
                .requestMatchers(
                        "/auth/register",
                        "/auth/login",
                        "/upload-and-get-url" // Assuming this is a public utility endpoint
                ).permitAll()
                // --- Student Endpoint Rules ---
                // Specific rules for TEACHER role
                .requestMatchers("/student/update/**", "/student/delete/**", "/student/view-all").hasRole("TEACHER")
                // Rule for both roles to view a specific student
                .requestMatchers("/student/view/**").hasAnyRole("STUDENT", "TEACHER")
                // Rule for both roles to register
                .requestMatchers("/student/register").hasAnyRole("STUDENT", "TEACHER")
                // All other requests must be authenticated
                .anyRequest().authenticated());
        httpSecurity.httpBasic(customizer -> customizer.disable());
        httpSecurity.formLogin(customizer -> customizer.disable());
        httpSecurity.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(authUserDetails);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
