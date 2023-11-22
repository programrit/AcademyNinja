package com.academy.ninja.user_controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.academy.ninja.entity.UserModel;
import com.academy.ninja.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/my-course")
@PreAuthorize("hasAuthority('ROLE_USER')")
public class MyCourseController {

	@Autowired
	private UserService userService;
	
	@GetMapping
	public String myCourse(Model model,HttpServletRequest request) {
		UserModel user = userService.findByEmail(request);
		if (user != null) {
			String error = (String) model.asMap().get("error");
			model.addAttribute("error", error);
			model.addAttribute("myCourses", userService.findAllMyCourse(user));
			model.addAttribute("title", "My Course");
			model.addAttribute("profile", user.getProfile());
			return "user/my_course";
		}else {
			return "redirect:/login";
		}

	}
}
