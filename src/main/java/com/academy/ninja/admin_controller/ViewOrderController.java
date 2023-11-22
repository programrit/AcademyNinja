package com.academy.ninja.admin_controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.academy.ninja.entity.UserModel;
import com.academy.ninja.service.AdminService;
import com.academy.ninja.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/view-order")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class ViewOrderController {

	@Autowired
	private UserService userService;
	@Autowired
	private AdminService adminService;
	
	@GetMapping
	public String order(Model model, HttpServletRequest request) {
		UserModel user = userService.findByEmail(request);
		if (user != null) {
			model.addAttribute("title", "View Order");
			model.addAttribute("courses", adminService.findAllOrder());
			model.addAttribute("adminProfile", user.getProfile());
			return "admin/view_order";
		} else {
			return "redirect:/login";
		}

	}
}
