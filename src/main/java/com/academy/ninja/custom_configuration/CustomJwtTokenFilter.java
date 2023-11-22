package com.academy.ninja.custom_configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.academy.ninja.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class CustomJwtTokenFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;
	@Autowired
	private CustomUserDetailService userDetailService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String cookieToken = getRefreshToken(request);
		HttpSession session = request.getSession();
		final String token = cookieToken;
		if (token != null) {
			if (!jwtService.isTokenExpired(token)) {
				final String accessTokenHeader = (String) session.getAttribute("Authorization");
				if (accessTokenHeader != null && accessTokenHeader.startsWith("Bearer ")) {
					String accessToken = accessTokenHeader.substring(7);
					if (!jwtService.isTokenExpired(accessToken)) {
						String username = jwtService.extractUsername(accessToken);
						CustomUserDetails userDetail = (CustomUserDetails) userDetailService
								.loadUserByUsername(username);
						if (jwtService.validateToken(accessToken, userDetail)) {
							if (isSecureUrl(request)) {
								if (username != null
										&& SecurityContextHolder.getContext().getAuthentication() == null) {
									System.out.println("This condition execute");
									UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
											userDetail, null, userDetail.getAuthorities());
									authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
									SecurityContextHolder.getContext().setAuthentication(authToken);
									session.setAttribute("Authorization", "Bearer " + accessToken);
									response.sendRedirect(request.getRequestURL().toString());
									return;
								}
							}
						} else {
							response.sendRedirect("/login");
							return;
						}
					} else {
						// generate new token
						String newAccessToken = jwtService.generateNewToken(token);
						if (newAccessToken != null) {
							session.setAttribute("Authorization", "Bearer " + newAccessToken);
							response.sendRedirect(request.getRequestURL().toString());
							return;
						} else {
							response.sendRedirect("/login");
							return;
						}
					}
				} else {
					String newAccessToken = jwtService.generateNewToken(token);
					if (newAccessToken != null) {
						session.setAttribute("Authorization", "Bearer " + newAccessToken);
						response.sendRedirect(request.getRequestURL().toString());
						return;
					} else {
						response.sendRedirect("/login");
						return;
					}
				}
			} else {
				// refresh token expired
				response.sendRedirect("/login");
				return;
			}

		} else {
			// refresh token not in the cookie
			response.sendRedirect("/login");
			return;
		}

		filterChain.doFilter(request, response);

	}

	

	private String getRefreshToken(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if(cookies !=null) {
			for(Cookie cookie: cookies) {
				if(cookie.getName().equals("refreshToken")) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}



	private boolean isSecureUrl(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		return requestURI.startsWith("/dashboard") || requestURI.startsWith("/profile")
				|| requestURI.startsWith("/update-password");
	}

}
