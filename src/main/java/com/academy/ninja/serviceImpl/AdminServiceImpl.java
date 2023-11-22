package com.academy.ninja.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.academy.ninja.entity.AdminModel;
import com.academy.ninja.entity.ContactModel;
import com.academy.ninja.entity.CourseModel;
import com.academy.ninja.entity.UserModel;
import com.academy.ninja.repository.CourseRepository;
import com.academy.ninja.repository.DBRepository;
import com.academy.ninja.repository.OrderRepository;
import com.academy.ninja.repository.UserContactRepository;
import com.academy.ninja.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService{

	@Autowired
	private final CourseRepository course;
	@Autowired
	private final DBRepository db;
	@Autowired
	private final OrderRepository order;
	@Autowired
	private final UserContactRepository contact;
	
	public AdminServiceImpl(CourseRepository course,DBRepository db,OrderRepository order,UserContactRepository contact) {
		this.course = course;
		this.db = db;
		this.order = order;
		this.contact = contact;
	}

	@Override
	public boolean saveCourse(AdminModel admin) {
		AdminModel adminData = course.save(admin);
		if(adminData !=null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean courseNameExist(String courseName) {
		AdminModel admin = course.findByCourseName(courseName);
		if(admin!=null) {
			return true;
		}
		return false;
	}

	@Override
	public List<AdminModel> findAllCourse() {
		return course.findAll();
	}

	@Override
	public List<UserModel> findAllUser() {
		return db.findAll().stream()
				.filter(user->"ROLE_USER".equals(user.getRole()))
				.collect(Collectors.toList());
	}

	@Override
	public List<UserModel> findAllAdmin() {
		return db.findAll().stream()
				.filter(user->"ROLE_ADMIN".equals(user.getRole()))
				.collect(Collectors.toList());
	}

	@Override
	public long findByNoCourse() {
		return course.count();
	}

	@Override
	public long findByNoUser(String role) {
		return db.countByRole(role);
	}

	@Override
	public long findByNoAdmin(String role) {
		return db.countByRole(role);
	}

	@Override
	public AdminModel findByCourseID(String courseId) {
		AdminModel admin = course.findByCourseId(courseId);
		if(admin !=null) {
			return admin;
		}
		return null;
	}

	@Override
	public void deleteByCourse(int courseId) {
		course.deleteById(courseId);
	}

	@Override
	public UserModel findByUser(String userId) {
		UserModel user = db.findByUserId(userId);
		if(user!=null) {
			return user;
		}
		return null;
	}

	@Override
	public void deleteByUser(int userId) {
		db.deleteById(userId);
	}

	@Override
	public void deleteByAdmin(int adminId) {
		db.deleteById(adminId);
	}

	@Override
	public boolean saveOrder(CourseModel course) {
		CourseModel courseModel = order.save(course);
		if(courseModel!=null) {
			return true;
		}
		return false;
	}

	@Override
	public long findByNoCourseBuy() {
		return order.count();
	}

	@Override
	public List<CourseModel> findAllOrder() {
		return order.findAll();
	}

	@Override
	public List<ContactModel> findAllContact() {
		return contact.findAll();
	}
	
	
}
