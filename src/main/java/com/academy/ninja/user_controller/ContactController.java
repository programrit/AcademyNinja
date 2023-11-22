package com.academy.ninja.user_controller;


import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.academy.ninja.entity.ContactModel;
import com.academy.ninja.entity.UserModel;
import com.academy.ninja.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/contact")
@PreAuthorize("hasAuthority('ROLE_USER')")
public class ContactController {
	
	@Autowired
	private UserService userService;
	
	private static final String messageValidation = "^[a-zA-Z0-9]+(?:\\s[a-zA-Z0-9]+)*$";

	@PostMapping
	public ResponseEntity<String> contact(@RequestParam String message,HttpServletRequest request,ContactModel contact) {
		if(message == null || message.isEmpty()) {
			return ResponseEntity.badRequest().body("Please enter a message");
		} else if(!Pattern.matches(messageValidation, message)){
			return ResponseEntity.badRequest().body("Please enter vaild message and only allowed alphabet and numbers");
		}else {
			UserModel user = userService.findByEmail(request);
			if(user!=null) {
				String name = user.getName();
				contact.setName(name);
				contact.setMessage(message);
				contact.setUser(user);
				if(userService.saveContact(contact)) {
					return ResponseEntity.ok("Message sent successfuly. Team will contact you shortly");
				}else {
					return ResponseEntity.badRequest().body("Something went wrong. Please try again later");
				}
				
			}else {
				return ResponseEntity.badRequest().body("User not found");
			}
		}
	}
}
