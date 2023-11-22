package com.academy.ninja.admin_controller;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.academy.ninja.entity.UserModel;
import com.academy.ninja.service.AdminService;
import com.academy.ninja.service.UserService;

import jakarta.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/admin-dashboard")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminDashboardController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AdminService adminService;
	
	private final static String regex = "^[a-z-A-Z0-9-]+$";

	@GetMapping
	public String dashboard(Model model,HttpServletRequest request) {
		UserModel user = userService.findByEmail(request); 
		if(user!=null) {
		model.addAttribute("title", "Dashboard");
		model.addAttribute("noCourse", adminService.findByNoCourse());
		model.addAttribute("noUser", adminService.findByNoUser("ROLE_USER"));
		model.addAttribute("noAdmin", adminService.findByNoAdmin("ROLE_ADMIN"));
		model.addAttribute("noBuyCourse", adminService.findByNoCourseBuy());
		model.addAttribute("users",adminService.findAllUser());
		model.addAttribute("adminProfile", user.getProfile());
		return "admin/admin_dashboard";
		}else {
			return "redirect:/login";
		}

	}
	
	@PostMapping
	public ResponseEntity<String> deleteUser(@RequestParam String userId){
		if(userId !=null) {
			if (Pattern.matches(regex, userId)) {
				UserModel user = adminService.findByUser(userId);
				if(user !=null) {
					try {
						adminService.deleteByUser(user.getId());
						return ResponseEntity.ok("User data deleted successfully");
					}catch(Exception e) {
						return ResponseEntity.badRequest().body("User data delete failed");
					}
				}else {
					return ResponseEntity.badRequest().body("User is not exist");
				}
			}else {
				return ResponseEntity.badRequest().body("Invalid user");
			}
		}else {
			return ResponseEntity.badRequest().body("Invalid user");
		}
	}
}
