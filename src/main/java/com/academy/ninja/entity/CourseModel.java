package com.academy.ninja.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="CourseOrder")
public class CourseModel {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String orderId;
	private String courseName;
	private String courseRandomId;
	
	private int coursePrice;
	
	@Column(name="payment_date")
	private LocalDateTime dateTime;
	
	@ManyToOne()
	@JoinColumn(name="courseId")
	private AdminModel admin;
	
	@ManyToOne()
	@JoinColumn(name="userId")
	private UserModel user;
	
	private boolean status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	

	public String getCourseRandomId() {
		return courseRandomId;
	}

	public void setCourseRandomId(String courseRandomId) {
		this.courseRandomId = courseRandomId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public AdminModel getAdmin() {
		return admin;
	}

	public void setAdmin(AdminModel admin) {
		this.admin = admin;
	}

	public int getCoursePrice() {
		return coursePrice;
	}

	public void setCoursePrice(int coursePrice) {
		this.coursePrice = coursePrice;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
	
	
	
	

}
