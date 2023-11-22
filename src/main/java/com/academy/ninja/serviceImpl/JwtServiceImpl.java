package com.academy.ninja.serviceImpl;


import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.academy.ninja.custom_configuration.CustomUserDetails;
import com.academy.ninja.service.JwtService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;


@Service
public class JwtServiceImpl implements JwtService{
	
	
	@Value("${jwt.secret.key}")
	private String SECRET;
	
	private static long ACCESS_TOKEN_EXPIRED = 1000*60*15;
	private static long REFRESH_TOKEN_EXPIRED = 1000*60*60*24*5L;
	
	

	@Override
	public String generateNewToken(String refreshToken ) {
		return createNewToken(refreshToken);
	}


	private String createNewToken(String refreshToken) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
			String accessNewToken = JWT.create()
					.withIssuer("auth0")
					.withSubject(extractUsername(refreshToken))
					.withClaim("role", extractRole(refreshToken))
					.withIssuedAt(new Date(System.currentTimeMillis()))
					.withExpiresAt(new Date(System.currentTimeMillis()+ACCESS_TOKEN_EXPIRED))
					.sign(algorithm);
			return accessNewToken;
		}catch(JWTCreationException e) {
			System.out.println("error1: "+e.getMessage());
			return null;
		}
	}
				

	public String extractUsername(String token) { 
		 DecodedJWT jwt = verifyToken(token);
		 if(jwt!=null) {
			 return jwt.getSubject();
		 }else {
			 return null;
		 }
	}
	
	private String extractRole(String token) {
		DecodedJWT jwt = verifyToken(token);
		 if(jwt!=null) {
			 return jwt.getClaim("role").toString();
		 }else {
			 return null;
		 }
	}
	
	

	private DecodedJWT verifyToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
			return JWT.require(algorithm).withIssuer("auth0").build().verify(token);
		}catch(JWTVerificationException e) {
			System.out.println("error2: "+e.getMessage());
			return null;
		}
	}

	@Override
	public boolean validateToken(String token, CustomUserDetails userDetails) {
		String username = extractUsername(token);
		if(username !=null) {
			return (username.equals(userDetails.getUsername()));
		}else {
			return false;
		}
	}

	@Override
	public boolean isTokenExpired(String token) {
		 DecodedJWT jwt = JWT.decode(token);
		 return jwt.getExpiresAt().before(new Date());
	}


	@Override
	public String refreshToken(String username, Collection<? extends GrantedAuthority> role) {
		Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
		String refreshToken = JWT.create()
				.withIssuer("auth0")
				.withSubject(username)
				.withClaim("role", role.toString())
				.withIssuedAt(new Date(System.currentTimeMillis()))
				.withExpiresAt(new Date(System.currentTimeMillis()+REFRESH_TOKEN_EXPIRED))
				.sign(algorithm);
		return refreshToken;
	}


}
	
