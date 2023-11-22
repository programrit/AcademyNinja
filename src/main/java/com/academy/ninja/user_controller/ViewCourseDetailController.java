package com.academy.ninja.user_controller;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.academy.ninja.entity.AdminModel;
import com.academy.ninja.entity.UserModel;
import com.academy.ninja.service.AdminService;
import com.academy.ninja.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/view-course-details")
public class ViewCourseDetailController {

	@Autowired
	private UserService userService;
	@Autowired
	private AdminService adminService;
	
	private final static String regex = "^[a-z-A-Z0-9-]+$";
	@GetMapping
	public String updateCourse(@RequestParam String ci,Model model, HttpServletRequest request,RedirectAttributes attributes) {
		UserModel user = userService.findByEmail(request);
		if (user != null) {
			if(ci==null || ci.isEmpty()) {
				attributes.addFlashAttribute("error", "Invalid course");
				return "redirect:/course";
			}else {
				if (Pattern.matches(regex, ci)) {
					AdminModel admin = adminService.findByCourseID(ci);
					if(admin!=null) {
						model.addAttribute("title", "Course Details");
						model.addAttribute("profile", user.getProfile());
						model.addAttribute("course", admin);
						return "user/view_course_details";
					}else {
						attributes.addFlashAttribute("error", "Course does not exist");
						return "redirect:/course";
					}
				}else {
					attributes.addFlashAttribute("error", "Invalid course");
					return "redirect:/course";
				}
			}
		}else {
			if(ci==null || ci.isEmpty()) {
				attributes.addFlashAttribute("error", "Invalid course");
				return "redirect:/course";
			}else {
				if (Pattern.matches(regex, ci)) {
					AdminModel admin = adminService.findByCourseID(ci);
					if(admin!=null) {
						model.addAttribute("title", "Course Details");
						model.addAttribute("course", admin);
						return "user/view_course_details";
					}else {
						attributes.addFlashAttribute("error", "Course is not exist");
						return "redirect:/course";
					}
				}else {
					attributes.addFlashAttribute("error", "Invalid course");
					return "redirect:/course";
				}
			}
		}

	}
	
}
