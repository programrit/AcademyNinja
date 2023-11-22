package com.academy.ninja.admin_controller;

import java.util.List;
import java.util.regex.Pattern;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.academy.ninja.entity.AdminModel;
import com.academy.ninja.entity.UserModel;
import com.academy.ninja.model.UpdateCourseModel;
import com.academy.ninja.service.AdminService;
import com.academy.ninja.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/update-course")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class UpdateCourseController {

	@Autowired
	private UserService userService;
	@Autowired
	private AdminService adminService;
	
	private final static String regex = "^[a-z-A-Z0-9-]+$";


	@GetMapping
	public String updateCourse(@RequestParam String ci,Model model, HttpServletRequest request,RedirectAttributes attributes) {
		UserModel user = userService.findByEmail(request);
		if (user != null) {
			if(ci==null || ci.isEmpty()) {
				attributes.addFlashAttribute("error", "Invalid course");
				return "redirect:/view-course";
			}else {
				if (Pattern.matches(regex, ci)) {
					AdminModel admin = adminService.findByCourseID(ci);
					if(admin!=null) {
						model.addAttribute("title", "Update Course");
						model.addAttribute("adminProfile", user.getProfile());
						model.addAttribute("course", admin);
						return "admin/update_course";
					}else {
						attributes.addFlashAttribute("error", "Course is not exist");
						return "redirect:/view-course";
					}
				}else {
					attributes.addFlashAttribute("error", "Invalid course");
					return "redirect:/view-course";
				}
			}
		}else {
			return "redirect:/login";
		}

	}
	
	
	
	
	@PostMapping
	public ResponseEntity<String> editCourse(@RequestBody @Valid UpdateCourseModel updateCourse,
			BindingResult updateCourseResult, Model model) {
		if (updateCourseResult.hasErrors()) {
			List<FieldError> errors = updateCourseResult.getFieldErrors();
			StringBuilder errorString = new StringBuilder();
			for (FieldError error : errors) {
				errorString.append(error.getDefaultMessage()).append("\n");
			}
			return ResponseEntity.badRequest().body(errorString.toString());
		}else {
			Object available = updateCourse.isAvailable();
			if(available.getClass() == Boolean.class) {
				AdminModel admin = adminService.findByCourseID(updateCourse.getToken());
				if(admin!=null) {
					admin.setAvailable(updateCourse.isAvailable());
					admin.setCoursePrice(updateCourse.getCoursePrice());
					admin.setShortDescription(updateCourse.getShortDescription());
					admin.setLongDescription(updateCourse.getLongDescription());
					if(adminService.saveCourse(admin)) {
						return ResponseEntity.ok("Course update successfully");
					}else {
						return ResponseEntity.badRequest().body("Course update failed. Please try again later");
					}
				}else {
					return ResponseEntity.badRequest().body("Invalid course");
				}
			}else {
				return ResponseEntity.badRequest().body("Please enter valid available (true or false)");
			}
		}
	}
}
