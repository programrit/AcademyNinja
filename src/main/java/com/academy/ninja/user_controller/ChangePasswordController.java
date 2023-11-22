package com.academy.ninja.user_controller;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.academy.ninja.entity.UserModel;
import com.academy.ninja.model.ChangePasswordModel;
import com.academy.ninja.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/change-password")
public class ChangePasswordController {
	
	@Autowired
	private UserService userService;
	
	private final static String regex = "^[a-z-A-Z0-9-]+$";

	@GetMapping
	public String verifyToken(@RequestParam String token, Model model, RedirectAttributes attributes) {
		if (Pattern.matches(regex, token)) {
			UserModel user = userService.findVerificationToken(token);
			if (user != null) {
				if (!user.isPasswordVerify()) {
					if (user.getExpiredTime() >= System.currentTimeMillis()) {
						user.setPasswordVerify(true);
						if (userService.saveData(user)) {
							model.addAttribute("title", "Change Password");
							return "user/change_password";
						} else {
							attributes.addFlashAttribute("error", "Token verification failed. Please try again later");
							return "redirect:/forgot-password";
						}
					} else {
						user.setPasswordVerify(false);
						user.setVerificationToken(null);
						if (userService.saveData(user)) {
							attributes.addFlashAttribute("error", "Link has been expired");
							return "redirect:/forgot-password";
						} else {
							attributes.addFlashAttribute("error", "Something went wrong. Please try again later");
							return "redirect:/forgot-password";
						}
					}
				} else {
					attributes.addFlashAttribute("error", "Token is already used");
					return "redirect:/forgot-password";
				}

			} else {
				attributes.addFlashAttribute("error", "Token is empty");
				return "redirect:/forgot-password";
			}
		} else {
			attributes.addFlashAttribute("error", "Invalid token");
			return "redirect:/forgot-password";
		}
	}

	@PostMapping
	public ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordModel changePassword,
			BindingResult changePasswordResult, Model model) {
		if (changePasswordResult.hasErrors()) {
			List<FieldError> errors = changePasswordResult.getFieldErrors();
			StringBuilder errorString = new StringBuilder();
			for (FieldError error : errors) {
				errorString.append(error.getDefaultMessage()).append("\n");
			}
			return ResponseEntity.badRequest().body(errorString.toString());
		} else {
			String password = changePassword.getPassword();
			String token = changePassword.getToken();
			if (Pattern.matches(regex, token)) {
				UserModel user = userService.findVerificationToken(token);
				if (user != null) {
					if (user.isPasswordVerify()) {
						if (user.getExpiredTime() >= System.currentTimeMillis()) {
							if (userService.CheckPassword(password, user)) {
								return ResponseEntity.badRequest()
										.body("Please use different passsword. Because this your old password");
							} else {
								String hashPassword = userService.passwordEncoder(password);
								if (hashPassword != null) {
									user.setPasswordVerify(false);
									user.setVerificationToken(null);
									user.setPassword(hashPassword);
									if (userService.saveData(user)) {
										return ResponseEntity.ok("Password change successfully");
									} else {
										return ResponseEntity.badRequest()
												.body("Token verification failed. Please try again later");
									}
								} else {
									return ResponseEntity.badRequest()
											.body("Something went wrong. Please try again later");
								}
							}
						} else {
							user.setPasswordVerify(false);
							user.setVerificationToken(null);
							if (userService.saveData(user)) {
								return ResponseEntity.badRequest().body("Link has been expired");
							} else {
								return ResponseEntity.badRequest()
										.body("Something went wrong. Please try again later.");
							}
						}
					} else {
						return ResponseEntity.badRequest().body("This link already used");
					}

				} else {
					return ResponseEntity.badRequest().body("Token is empty");
				}
			} else {
				return ResponseEntity.badRequest().body("Invalid token");
			}
		}
	}
}
