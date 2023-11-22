package com.academy.ninja.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ForgotPasswordModel {

	@NotBlank(message="Please enter your email address")
	@Email(message ="Please enter valid email address")
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
