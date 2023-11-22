package com.academy.ninja.admin_controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.academy.ninja.entity.UserModel;
import com.academy.ninja.model.UpdatePasswordModel;
import com.academy.ninja.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/settings")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class SettingsController {

	@Autowired
	private UserService userService;
	
	
	@GetMapping
	public String settings(Model model, HttpServletRequest request) {
		UserModel user = userService.findByEmail(request);
		if (user != null) {
			model.addAttribute("title", "Settings");
			model.addAttribute("adminData", user);
			model.addAttribute("adminProfile", user.getProfile());
			return "admin/settings";
		} else {
			return "redirect:/login";
		}
	}
	
	@PostMapping
	public ResponseEntity<String> adminPasswordUpdate(@RequestBody @Valid UpdatePasswordModel updatePassword,
			BindingResult updatePasswordResult, HttpServletRequest request, HttpServletResponse response) {
		if (updatePasswordResult.hasErrors()) {
			List<FieldError> errors = updatePasswordResult.getFieldErrors();
			StringBuilder errorString = new StringBuilder();
			for (FieldError error : errors) {
				errorString.append(error.getDefaultMessage()).append("\n");
			}
			return ResponseEntity.badRequest().body(errorString.toString());
		} else {
			String oldPassword = updatePassword.getOldPassword();
			String newPassword = updatePassword.getNewPassword();
			UserModel user = userService.findByEmail(request);
			if(user!=null) {
				if (userService.updatePassword(oldPassword, newPassword, user)) {
					if (oldPassword.equals(newPassword)) {
						return ResponseEntity.badRequest()
								.body("Please use different password, Because new password is same us old password");
					} else {
						user.setPassword(userService.passwordEncoder(newPassword));
						if (userService.saveData(user)) {
							if (userService.deleteCookieAndRememberMe(request, response)) {
								return ResponseEntity.ok("Password update successfully");
							} else {
								System.out.println("not delete cookie");
								return ResponseEntity.ok("Password update successfully");
							}
						} else {
							return ResponseEntity.badRequest().body("Password update failed. Please try again later");
						}
					}
				} else {
					return ResponseEntity.badRequest().body("Incorrect old password");
				}
			} else {
				return ResponseEntity.badRequest().body("Unautorized person");
			}
		}
	}
}
