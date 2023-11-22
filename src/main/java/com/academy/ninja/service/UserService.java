package com.academy.ninja.service;

import java.util.List;


import com.academy.ninja.entity.AdminModel;
import com.academy.ninja.entity.ContactModel;
import com.academy.ninja.entity.CourseModel;
import com.academy.ninja.entity.UserModel;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

	UserModel createUser(UserModel user);
	UserModel findVerificationToken(String verificationToken);
	UserModel findByEmail(HttpServletRequest request);
	UserModel findByGmail(String email);
	UserModel addAdmin(UserModel user);
	CourseModel checkOrderId(String orderId);
	CourseModel existUserCourse(AdminModel admin,UserModel user);
	String passwordEncoder(String password);
	boolean saveContact(ContactModel contact);
	boolean CheckPassword(String password,UserModel user);
	boolean emailExist(String email);
	boolean saveData(UserModel user);
	boolean updatePassword(String oldPassword, String newPassword,UserModel user);
	boolean deleteCookieAndRememberMe(HttpServletRequest request,HttpServletResponse response);
	boolean saveOrder(CourseModel course);
	List<CourseModel> findAllMyCourse(UserModel user);
}
