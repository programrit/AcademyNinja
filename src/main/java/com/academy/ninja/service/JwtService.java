package com.academy.ninja.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.academy.ninja.custom_configuration.CustomUserDetails;


public interface JwtService {
	
	String refreshToken(String username,Collection<? extends GrantedAuthority> role);
	
	String generateNewToken(String refreshToken);
	
	String extractUsername(String token);
	
	boolean validateToken(String token, CustomUserDetails userDetails);
	
	boolean isTokenExpired(String token);
	
}
