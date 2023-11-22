package com.academy.ninja.user_controller;

import java.util.List;
import java.util.UUID;

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

import com.academy.ninja.entity.UserModel;
import com.academy.ninja.model.ForgotPasswordModel;
import com.academy.ninja.service.EmailService;
import com.academy.ninja.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/forgot-password")
public class ForgotPasswordController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private EmailService emailService;
	
	@GetMapping
	public String forgotPassord(Model model) {
		String error = (String) model.asMap().get("error");
		model.addAttribute("error", error);
		model.addAttribute("title", "Fogot Password");
		return "forgot_password";
	}
	
	@PostMapping
	public ResponseEntity<String> forgotPassword(@RequestBody @Valid ForgotPasswordModel forgetPasswordModel,
			BindingResult forgetPasswordResult) {
		if (forgetPasswordResult.hasErrors()) {
			List<FieldError> errors = forgetPasswordResult.getFieldErrors();
			StringBuilder errorString = new StringBuilder();
			for (FieldError error : errors) {
				errorString.append(error.getDefaultMessage()).append("\n");
			}
			return ResponseEntity.badRequest().body(errorString.toString());
		} else {
			System.out.println(forgetPasswordModel.getEmail());
			UserModel user = userService.findByGmail(forgetPasswordModel.getEmail());
			if (user != null) {
				String token = UUID.randomUUID().toString();
				user.setExpiredTime(System.currentTimeMillis() + (24 * 60 * 60 * 1000));
				user.setVerificationToken(token);
				user.setPasswordVerify(false);
				String link = "http://localhost:8080/change-password?token=" + token;
				String text = "Hello " + user.getName() + "\n\n" + "Your change password link is here " + link + "\n\n"
						+ "Link is expired after 24hours";
				if (emailService.sendMail(user.getEmail(), "Change Password", text)) {
					if (userService.saveData(user)) {
						return ResponseEntity.ok("Password change link send successfully");
					} else {
						return ResponseEntity.badRequest().body("Something went wrong. Please try again later");
					}
				} else {
					return ResponseEntity.badRequest().body("Password change link send failed");
				}
			} else {
				return ResponseEntity.badRequest().body("User is not found!");
			}
		}
	}

}
