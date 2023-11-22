package com.academy.ninja.user_controller;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.academy.ninja.entity.AdminModel;
import com.academy.ninja.entity.CourseModel;
import com.academy.ninja.entity.UserModel;
import com.academy.ninja.service.AdminService;
import com.academy.ninja.service.PaymentService;
import com.academy.ninja.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/course")
public class CourseController {

	@Autowired
	private UserService userService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private PaymentService payment;

	@Value("${razorpay.api.key}")
	private String apiKey;

	private final static String regex = "^[a-z-A-Z0-9-]+$";

	@GetMapping
	public String dashboard(Model model, HttpServletRequest request) {
		UserModel user = userService.findByEmail(request);
		if (user != null) {
			String error = (String) model.asMap().get("error");
			model.addAttribute("noCourse", adminService.findByNoCourse());
			model.addAttribute("courses", adminService.findAllCourse());
			model.addAttribute("error", error);
			model.addAttribute("title", "Course");
			model.addAttribute("profile", user.getProfile());
			model.addAttribute("apiKey", apiKey);
			return "user/jobs";
		} else {
			model.addAttribute("noCourse", adminService.findByNoCourse());
			model.addAttribute("courses", adminService.findAllCourse());
			model.addAttribute("title", "Course");
			return "user/jobs";
		}
	}

	@PostMapping
	public ResponseEntity<String> payment(@RequestParam String courseId, HttpServletRequest request,
			CourseModel course) {
		UserModel user = userService.findByEmail(request);
		if (user != null) {
			if (courseId == null || courseId.isEmpty()) {
				return ResponseEntity.badRequest().body("Invalid course");
			} else {
				if (Pattern.matches(regex, courseId)) {
					AdminModel admin = adminService.findByCourseID(courseId);
					if (admin != null) {
						CourseModel courseModel = userService.existUserCourse(admin,user);
						if (courseModel!=null) {
							if(courseModel.isStatus()) {
								return ResponseEntity.badRequest().body("Sorry. You have already buy this course.");
							}else {
								return ResponseEntity.badRequest().body("Payment is failed. Please contact team");
							}
						} else {
							int price = Integer.parseInt(admin.getCoursePrice());
							String orderId = payment.createOrder(price, "INR");
							if (orderId != null) {
								LocalDateTime current_date_time = LocalDateTime.now();
								course.setAdmin(admin);
								course.setCourseRandomId(courseId);
								course.setOrderId(orderId);
								course.setCourseName(admin.getCourseName());
								course.setCoursePrice(price);
								course.setDateTime(current_date_time);
								course.setUser(user);
								if (adminService.saveOrder(course)) {
									return ResponseEntity
											.ok("{\"orderId\":\"" + orderId + "\",\"amount\":" + price + "}");
								} else {
									return ResponseEntity.badRequest()
											.body("Something went wrong.Please try again later");
								}

							} else {
								return ResponseEntity.badRequest().body("Something went wrong. Please try again later");
							}
						}
					} else {
						return ResponseEntity.badRequest().body("Course does not exist!");
					}
				} else {
					return ResponseEntity.badRequest().body("Invalid course");
				}
			}
		} else {
			return ResponseEntity.badRequest().body("Please login after buy a course.");
		}
	}
}
