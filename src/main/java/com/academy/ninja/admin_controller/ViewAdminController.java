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
@RequestMapping("/view-admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class ViewAdminController {

	@Autowired
	private UserService userService;

	@Autowired
	private AdminService adminService;
	
	private final static String regex = "^[a-z-A-Z0-9-]+$";

	@GetMapping
	public String dashboard(Model model, HttpServletRequest request) {
		UserModel user = userService.findByEmail(request);
		if (user != null) {
			model.addAttribute("title", "View Admin");
			model.addAttribute("admins", adminService.findAllAdmin());
			model.addAttribute("adminProfile", user.getProfile());
			return "admin/view_admin";
		} else {
			return "redirect:/login";
		}

	}
	
	@PostMapping
	public ResponseEntity<String> deleteUser(@RequestParam String adminId){
		if(adminId !=null) {
			if (Pattern.matches(regex, adminId)) {
				UserModel user = adminService.findByUser(adminId);
				if(user !=null) {
					try {
						adminService.deleteByAdmin(user.getId());
						return ResponseEntity.ok("Admin data deleted successfully");
					}catch(Exception e) {
						return ResponseEntity.badRequest().body("Admin data delete failed");
					}
				}else {
					return ResponseEntity.badRequest().body("Admin is not exist");
				}
			}else {
				return ResponseEntity.badRequest().body("Invalid admin");
			}
		}else {
			return ResponseEntity.badRequest().body("Invalid admin");
		}
	}
}
