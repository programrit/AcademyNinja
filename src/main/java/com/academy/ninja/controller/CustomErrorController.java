package com.academy.ninja.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.academy.ninja.entity.UserModel;
import com.academy.ninja.service.UserService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/error")
public class CustomErrorController implements ErrorController{
	
	@Autowired
	private UserService userService;

	@GetMapping
	public String habdleError(HttpServletRequest request,Model model,HttpServletResponse response) {
		UserModel user = userService.findByEmail(request);
		if (user != null) {
			Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
			if(status !=null) {
				int statusCode = Integer.parseInt(status.toString());
				System.out.println("role: "+user.getRole().toString());
				if(user.getRole().equals("ROLE_USER")) {
					if(statusCode == HttpStatus.NOT_FOUND.value()) {
						model.addAttribute("title", "404");
						return "user_error/404";
					} else if(statusCode == HttpStatus.FORBIDDEN.value()) {
						model.addAttribute("title", "403");
						return "user_error/403";
					} else if(statusCode == HttpStatus.BAD_REQUEST.value()) {
						model.addAttribute("title", "400");
						return "user_error/400";
					} else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
						model.addAttribute("title", "500");
						return "user_error/400";
					} else {
						model.addAttribute("title", "Default Error");
						return "default";
					}
				}else {
					if(statusCode == HttpStatus.NOT_FOUND.value()) {
						model.addAttribute("title", "404");
						return "admin_error/404";
					} else if(statusCode == HttpStatus.FORBIDDEN.value()) {
						model.addAttribute("title", "403");
						return "admin_error/403";
					} else if(statusCode == HttpStatus.BAD_REQUEST.value()) {
						model.addAttribute("title", "400");
						return "admin_error/400";
					} else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
						model.addAttribute("title", "500");
						return "admin_error/500";
					} else {
						model.addAttribute("title", "Default Error");
						return "default";
					}
				}
			}else {
				return request.getRequestURL().toString();
			}
		}else {
			return "redirect:/login";
		}
	}
	
	
}
