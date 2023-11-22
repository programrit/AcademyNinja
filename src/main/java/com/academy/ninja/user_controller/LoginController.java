package com.academy.ninja.user_controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

	@GetMapping
	public String login(Model model) {
		model.addAttribute("title", "Login");
		return "login";
	}
}
