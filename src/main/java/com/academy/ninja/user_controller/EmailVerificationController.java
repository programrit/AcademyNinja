package com.academy.ninja.user_controller;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.academy.ninja.entity.UserModel;
import com.academy.ninja.service.UserService;

@Controller
@RequestMapping("/verify")
public class EmailVerificationController {
	
	@Autowired
	private UserService userService;

	private final static String regex = "^[a-z-A-Z0-9-]+$";
	
	@GetMapping
	public String verifyUser(@RequestParam String token, Model model) {
		if (Pattern.matches(regex, token)) {
			UserModel user = userService.findVerificationToken(token);
			if (user != null) {
				if (!user.isVerify()) {
					if (user.getExpiredTime() >= System.currentTimeMillis()) {
						user.setVerify(true);
						user.setVerificationToken(null);
						if (userService.saveData(user)) {
							model.addAttribute("success", "Email verification successfully");
						} else {
							model.addAttribute("error", "Email verification failed");
						}
					} else {
						user.setVerificationToken(null);
						if (userService.saveData(user)) {
							model.addAttribute("error", "Link has been expired");
						} else {
							model.addAttribute("error", "Something went wrong. Please try again later");
						}
					}
				} else {
					model.addAttribute("error", "Email already verified");
				}

			} else {
				model.addAttribute("error", "Invalid token");
			}
		} else {
			model.addAttribute("error", "Invalid token");
		}
		return "success";
	}

}
