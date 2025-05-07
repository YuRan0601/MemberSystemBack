package com.yuran.hotel.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestBody;

import com.yuran.hotel.user.security.JwtFilter;

import jakarta.annotation.Resource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	@Resource
	private JwtFilter jwtFilter;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(auth -> {
			auth
			.requestMatchers("/auth/**").permitAll()
			.requestMatchers("/admin/**").hasRole("admin")
			.requestMatchers("/user/**").hasAnyRole("user", "admin")
			.anyRequest().authenticated();
		})
		.sessionManagement(session -> {
			session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		})
		.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		
		
		return http.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
