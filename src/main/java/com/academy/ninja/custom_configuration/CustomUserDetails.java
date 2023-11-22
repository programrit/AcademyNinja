package com.academy.ninja.custom_configuration;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.academy.ninja.entity.UserModel;


@SuppressWarnings("serial")
public class CustomUserDetails implements UserDetails{

	@Autowired
	private final UserModel user;
	
	public CustomUserDetails(UserModel user) {
		super();
		this.user =user;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority auth = new SimpleGrantedAuthority(user.getRole());
		return Arrays.asList(auth);
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.isVerify();
	}

}
