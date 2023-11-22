package com.academy.ninja.admin_controller;

import java.util.List;
import java.util.UUID;

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
import com.academy.ninja.service.EmailService;
import com.academy.ninja.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/add-user")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AddUserController {

	@Autowired
	private UserService userService;
	@Autowired
	private EmailService emailService;

	@GetMapping
	public String addUser(Model model, HttpServletRequest request) {
		UserModel user = userService.findByEmail(request);
		if (user != null) {
			model.addAttribute("title", "Add User");
			model.addAttribute("adminProfile", user.getProfile());
			return "admin/add_user";
		} else {
			return "redirect:/login";
		}
	}

	@PostMapping
	public ResponseEntity<String> addNewUser(@RequestBody @Valid UserModel user, BindingResult userResult) {
		if (userResult.hasErrors()) {
			List<FieldError> errors = userResult.getFieldErrors();
			StringBuilder errorString = new StringBuilder();
			for (FieldError error : errors) {
				errorString.append(error.getDefaultMessage()).append("\n");
			}
			return ResponseEntity.badRequest().body(errorString.toString());
		} else {
			user.setUserId(UUID.randomUUID().toString());
			if (userService.emailExist(user.getEmail())) {
				return ResponseEntity.badRequest().body("Email is already exist");
			} else {
				String token = UUID.randomUUID().toString();
				user.setExpiredTime(System.currentTimeMillis() + (24 * 60 * 60 * 1000));
				user.setVerificationToken(token);
				String link = "http://localhost:8080/verify?token=" + token;
				String text = "Hello " + user.getName() + "\n\n" + "Your verification is here " + link + "\n\n"
						+ "Link is expired after 24hours";
				if (emailService.sendMail(user.getEmail(), "Email Verification", text)) {
					UserModel addUser = userService.createUser(user);
					if (addUser != null) {
						return ResponseEntity.ok("User add successfully. Verfication link send your mail");
					} else {
						return ResponseEntity.badRequest().body("User add failed. Please try again later");
					}
				} else {
					return ResponseEntity.badRequest().body("Verification link send failed");
				}
			}

		}
	}

}
