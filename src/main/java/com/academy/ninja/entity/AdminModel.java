package com.academy.ninja.entity;


import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="Course")
public class AdminModel {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String courseId;
	
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
	
	private boolean available;
	
	@NotNull(message="Please upload videos")
	private List<String> courseVideos;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

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

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public List<String> getCourseVideos() {
		return courseVideos;
	}

	public void setCourseVideos(List<String> courseVideos) {
		this.courseVideos = courseVideos;
	}


	
	
	
	
	
	
	
}
