package com.academy.ninja.user_controller;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.academy.ninja.entity.AdminModel;
import com.academy.ninja.entity.CourseModel;
import com.academy.ninja.entity.UserModel;
import com.academy.ninja.service.AdminService;
import com.academy.ninja.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/view-course-video")
@PreAuthorize("hasAuthority('ROLE_USER')")
public class ViewCourseVideoController {

	@Autowired
	private UserService userService;
	@Autowired
	private AdminService adminService;

	private final static String regex = "^[a-z-A-Z0-9-]+$";

	@GetMapping
	public String viewCoureVideo(@RequestParam String ci, Model model, HttpServletRequest request,
			RedirectAttributes attributes) {
		UserModel user = userService.findByEmail(request);
		if (user != null) {
			if (ci == null || ci.isEmpty()) {
				attributes.addFlashAttribute("error", "Invalid course");
				return "redirect:/my-course";
			} else {
				if (Pattern.matches(regex, ci)) {
					AdminModel admin = adminService.findByCourseID(ci);
					CourseModel courseModel = userService.existUserCourse(admin, user);
					if (courseModel != null) {
						if (courseModel.isStatus()) {
							model.addAttribute("title", admin.getCourseName()+" videos");
							model.addAttribute("myCourseVideo", admin.getCourseVideos());
							return "user/view_course_video";
						} else {
							attributes.addFlashAttribute("error", "Payment is not completed");
							return "redirect:/my-course";
						}
					} else {
						attributes.addFlashAttribute("error", "Course not found");
						return "redirect:/my-course";
					}
				} else {
					attributes.addFlashAttribute("error", "Invalid course");
					return "redirect:/my-course";
				}
			}
		} else {
			return "redirect:/login";
		}
	}

}
