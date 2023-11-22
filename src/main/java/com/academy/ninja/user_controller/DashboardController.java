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
@RequestMapping("/dashboard")
@PreAuthorize("hasAuthority('ROLE_USER')")
public class DashboardController {
	
	@Autowired
	private UserService userService;

	
	@GetMapping
	public String dashboard(Model model, HttpServletRequest request) {
		UserModel user = userService.findByEmail(request);
		if (user != null) {
			model.addAttribute("profile", user.getProfile());
			model.addAttribute("title", "Dashboard");
			return "user/dashboard";
		} else {
			return "redirect:/login";
		}

	}

}
