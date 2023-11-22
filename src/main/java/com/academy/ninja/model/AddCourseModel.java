package com.academy.ninja.model;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

public class AddCourseModel {

	@NotBlank(message="Please enter a course name")
	@Pattern(regexp="[a-z-A-Z ]+$",message="Please enter valid course name")
	private String courseName;
	
	@NotBlank(message="Please enter a course price")
	@Pattern(regexp="[0-9]+$",message="Please enter valid course price")
	private String coursePrice;
	
	@NotBlank(message="Please enter a short description")
	@Size(max=255,message="Short description only allowed 255 character")
	@Column(name="short_description",length=255)
	private String shortDescription;
	
	@NotBlank(message="Please enter a long description")
	@Size(max=2000,message="Long description only allowed 2000 character")
	@Column(name="long_description",length=2000)
	private String longDescription;
	
	@NotNull(message="Please upload videos")
	@Size(max=10,message="You can upload 10 videos only")
	private MultipartFile[] videos;

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

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

	public MultipartFile[] getVideos() {
		return videos;
	}

	public void setVideos(MultipartFile[] videos) {
		this.videos = videos;
	}
	
	
	

}
