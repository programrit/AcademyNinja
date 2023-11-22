package com.academy.ninja.admin_controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.academy.ninja.entity.AdminModel;
import com.academy.ninja.entity.UserModel;
import com.academy.ninja.service.AdminService;
import com.academy.ninja.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/view-course")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class ViewCourseController {

	@Autowired
	private UserService userService;
	@Autowired
	private AdminService adminService;
	
	private final static String regex = "^[a-z-A-Z0-9-]+$";
	
	@Value("${custom.video.path}")
	private String EXTERNAL_VIDEO_PATH;

	@GetMapping
	public String dashboard(Model model, HttpServletRequest request) {
		UserModel user = userService.findByEmail(request);
		if (user != null) {
			String error = (String) model.asMap().get("error");
			model.addAttribute("error", error);
			model.addAttribute("title", "View Course");
			model.addAttribute("courses", adminService.findAllCourse());
			model.addAttribute("adminProfile", user.getProfile());
			return "admin/view_course";
		} else {
			return "redirect:/login";
		}

	}
	
	
	@PostMapping
	public ResponseEntity<String> deleteCourse(@RequestParam String courseId){
		if(courseId !=null) {
			if (Pattern.matches(regex, courseId)) {
				AdminModel admin = adminService.findByCourseID(courseId);
				if(admin !=null) {
					try {
						List<String> courseVideos = admin.getCourseVideos();
						adminService.deleteByCourse(admin.getId());
						for(String video:courseVideos) {
							Path videoFilePath = Paths.get(EXTERNAL_VIDEO_PATH, video);
							if(Files.exists(videoFilePath)) {
								Files.delete(videoFilePath);
							}
						}
						return ResponseEntity.ok("Course deleted successfully");
					}catch(Exception e) {
						return ResponseEntity.badRequest().body("Course delete failed. Becasue using foreign key");
					}
				}else {
					return ResponseEntity.badRequest().body("Course is not exist");
				}
			}else {
				return ResponseEntity.badRequest().body("Invalid course");
			}
		}else {
			return ResponseEntity.badRequest().body("Invalid course");
		}
	}

}
