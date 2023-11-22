package com.academy.ninja.custom_configuration;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.academy.ninja.service.JwtService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

	
	private final JwtService jwtService;
	public CustomAuthenticationSuccessHandler(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		try {
			String username = userDetails.getUsername();
			Collection<? extends GrantedAuthority> role = userDetails.getAuthorities();
			String authToken = jwtService.refreshToken(username, role);
			Cookie cookie = new Cookie("refreshToken",authToken);
			cookie.setMaxAge(10*24*60*60);
			cookie.setSecure(true);
			cookie.setPath("/");
			response.addCookie(cookie);
			if(role.toString().equals("[ROLE_ADMIN]")) {
				System.out.println("role1: "+role.toString());
				response.sendRedirect("/admin-dashboard");
			}else {
				System.out.println("role2: "+role.toString());
				response.sendRedirect("/dashboard");
			}
		}catch(Exception e) {
			e.printStackTrace();
			response.sendRedirect("/login");
		}
		
	}

}
