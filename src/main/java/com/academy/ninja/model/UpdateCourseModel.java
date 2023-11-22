package com.academy.ninja.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UpdateCourseModel {

	@NotBlank(message="Please enter a course price")
	@Pattern(regexp="[0-9]+$",message="Please enter valid course price")
	private String coursePrice;
	
	@NotBlank(message="Please enter a short description")
	private String shortDescription;
	
	@NotBlank(message="Please enter a long description")
	private String longDescription;
	
	private boolean available;
	
	@NotBlank(message="Invalid course")
	private String token;

	public String getCoursePrice() {
		return coursePrice;
	}

	public void setCoursePrice(String coursePrice) {
		this.coursePrice = coursePrice;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
}
