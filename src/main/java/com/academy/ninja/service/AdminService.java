package com.academy.ninja.service;

import java.util.List;


import com.academy.ninja.entity.AdminModel;
import com.academy.ninja.entity.ContactModel;
import com.academy.ninja.entity.CourseModel;
import com.academy.ninja.entity.UserModel;


public interface AdminService {

	List<AdminModel> findAllCourse();
	List<UserModel> findAllUser();
	List<UserModel> findAllAdmin();
	List<CourseModel> findAllOrder();
	List<ContactModel> findAllContact();
	AdminModel findByCourseID(String courseId);
	UserModel findByUser(String userId);
	long findByNoCourse();
	long findByNoUser(String role);
	long findByNoAdmin(String role);
	long findByNoCourseBuy();
	boolean saveCourse(AdminModel admin);
	boolean saveOrder(CourseModel course);
	boolean courseNameExist(String courseName);
	void deleteByCourse(int courseId);
	void deleteByUser(int userId);
	void deleteByAdmin(int adminId);
}
