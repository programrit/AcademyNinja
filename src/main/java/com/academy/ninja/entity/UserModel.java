package com.academy.ninja.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
@Entity
@Table(name="User")
public class UserModel {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String userId;
	
	@NotBlank(message="Please enter a name")
	@Pattern(regexp="[a-z-A-Z]+",message="Please enter valid name")
	private String name;
	
	@NotBlank(message="Please enter your email address")
	@Email(message ="Please enter valid email address")
	private String email;
	
	@NotBlank(message="Please enter a password")
	@Pattern(regexp="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$",message="Please enter strong password")
	private String password;
	

	private String verificationToken;
	
	private boolean verify;
	
	private long expiredTime;

	
	private boolean passwordVerify;
	
	private String role;
	
	private String profile;
	
	
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getVerificationToken() {
		return verificationToken;
	}
	public void setVerificationToken(String verificationToken) {
		this.verificationToken = verificationToken;
	}
	public boolean isVerify() {
		return verify;
	}
	public void setVerify(boolean verify) {
		this.verify = verify;
	}
	public boolean isPasswordVerify() {
		return passwordVerify;
	}
	public void setPasswordVerify(boolean passwordVerify) {
		this.passwordVerify = passwordVerify;
	}
	public long getExpiredTime() {
		return expiredTime;
	}
	public void setExpiredTime(long expiredTime) {
		this.expiredTime = expiredTime;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

}
