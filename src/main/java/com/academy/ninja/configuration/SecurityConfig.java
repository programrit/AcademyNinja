package com.academy.ninja.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.academy.ninja.custom_configuration.CustomAuthenticationSuccessHandler;
import com.academy.ninja.custom_configuration.CustomJwtTokenFilter;
import com.academy.ninja.custom_configuration.CustomUserDetailService;
import com.academy.ninja.service.JwtService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtService jwtService;

	private final CustomJwtTokenFilter jwtTokenFilter;

	@Value("${jwt.secret.key}")
	private String SECRET;

	public SecurityConfig(CustomJwtTokenFilter jwtTokenFilter) {
		this.jwtTokenFilter = jwtTokenFilter;
	}

	@Bean
	FilterRegistrationBean<CustomJwtTokenFilter> jwtFilter() {
		FilterRegistrationBean<CustomJwtTokenFilter> filter = new FilterRegistrationBean<>();
		filter.setFilter(jwtTokenFilter);
		filter.addUrlPatterns("/dashboard", "/profile", "/update-password", "/logout", "/contact", "/my-course");
		filter.addUrlPatterns("/admin-dashboard", "/add-admin", "/add-user", "/add-course", "/view-admin",
				"/view-course", "/admin-profile", "/settings", "/update-course", "/view-order", "/view-contact");
		return filter;
	}

	@Bean
	UserDetailsService userDetailsService() {
		return new CustomUserDetailService();
	}

	@Bean
	BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userDetailsService());
		auth.setPasswordEncoder(encoder());
		return auth;
	}

	@Bean
	CsrfTokenRepository csrfToken() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-CSRF-TOKEN");
		return repository;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	RememberMeServices rememberMe() {
		TokenBasedRememberMeServices remember = new TokenBasedRememberMeServices(SECRET, userDetailsService());
		remember.setParameter("remember-me");
		remember.setAlwaysRemember(true);
		remember.setUseSecureCookie(true);
		remember.setTokenValiditySeconds(10 * 24 * 60 * 60);
		return remember;
	}

	@Bean
	SecurityFilterChain security(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> {
			csrf.csrfTokenRepository(csrfToken());
			csrf.ignoringRequestMatchers("/payment");
		}).authorizeHttpRequests(auth -> {
			auth.requestMatchers("/", "/login", "/signup", "/js/**", "/css/**", "/images/**", "/verify",
					"/authentication", "/forgot-password", "/change-password", "/course", "/error",
					"/view-course-details").permitAll();
			auth.requestMatchers("/dashboard", "/profile", "/update-password", "/logout", "/contact", "/my-course",
					"/payment", "/payment/**","/view-course-video").hasRole("USER")
					.requestMatchers("/admin-dashboard", "/add-admin", "/add-user", "/add-course", "/view-admin",
							"/view-course", "/logout", "/admin-profile", "/settings", "/update-course", "/view-order",
							"/view-contact")
					.hasRole("ADMIN").anyRequest().authenticated();
		}).sessionManagement(session -> {
			session.sessionFixation().migrateSession().maximumSessions(1).maxSessionsPreventsLogin(false)
					.expiredUrl("/login");
			session.invalidSessionUrl("/login");
		}).logout(logout -> {
			logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).invalidateHttpSession(true)
					.clearAuthentication(true).deleteCookies("refreshToken").deleteCookies("remember-me")
					.logoutSuccessUrl("/login");
		}).formLogin(login -> {
			login.loginPage("/login").loginProcessingUrl("/authentication")
					.successHandler(new CustomAuthenticationSuccessHandler(jwtService)).permitAll();
		}).rememberMe(remember -> {
			remember.key(SECRET).rememberMeServices(rememberMe());
		}).exceptionHandling(exception -> {
			exception.accessDeniedPage("/error");
		}).httpBasic(Customizer.withDefaults()).build();
	}

}
