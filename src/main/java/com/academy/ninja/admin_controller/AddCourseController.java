package com.academy.ninja.admin_controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.academy.ninja.entity.AdminModel;
import com.academy.ninja.entity.UserModel;
import com.academy.ninja.model.AddCourseModel;
import com.academy.ninja.service.AdminService;
import com.academy.ninja.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/add-course")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AddCourseController {

	@Autowired
	private UserService userService;

	@Autowired
	private AdminService adminService;

	@Value("${custom.video.path}")
	private String EXTERNAL_VIDEO_PATH;

	private static final List<String> ALLOWED_EXTENSION = Arrays.asList("mp4");
	private static final long MAX_SIZE = 100 * 1024 * 1024;

	@GetMapping
	public String addCourse(Model model, HttpServletRequest request) {
		UserModel user = userService.findByEmail(request);
		if (user != null) {
			model.addAttribute("title", "Add Course");
			model.addAttribute("adminProfile", user.getProfile());
			return "admin/add_course";
		} else {
			return "redirect:/login";
		}

	}
	@PostMapping
	@ResponseBody
	public ResponseEntity<List<String>> addNewCourse(@ModelAttribute @Valid AddCourseModel addCourse, BindingResult addCourseResult,AdminModel admin) {
		if (addCourseResult.hasErrors()) {
			List<FieldError> errors = addCourseResult.getFieldErrors();
			List<String> errorString = new ArrayList<>();
			for (FieldError error : errors) {
				errorString.add(error.getDefaultMessage());
			}
			return ResponseEntity.badRequest().body(errorString);
		} else {
			MultipartFile[] videos = addCourse.getVideos();
			List<String> uploadFiles = new ArrayList<>();
			if (videos.length <= 0) {
				return ResponseEntity.badRequest().body(Arrays.asList("Please upload videos"));
			}else if (videos.length > 10) {
				return ResponseEntity.badRequest().body(Arrays.asList("You can upload 10 videos only"));
			} else {
				for (MultipartFile video : videos) {
					String fileName = video.getOriginalFilename();
					String extension = fileName != null ? fileName.substring(fileName.lastIndexOf(".") + 1) : "";
					if (!ALLOWED_EXTENSION.contains(extension.toLowerCase())) {
						return ResponseEntity.badRequest().body(Arrays.asList("Please upload mp4 format videos only"));
					}
					if (video.getSize() > MAX_SIZE) {
						return ResponseEntity.badRequest().body(Arrays.asList("Video size grater than 100MB"));
					}
					String newFileName = UUID.randomUUID().toString() + "." + extension;
					String filePath = EXTERNAL_VIDEO_PATH + File.separator + newFileName;
					try {
						video.transferTo(new File(filePath));
						uploadFiles.add(newFileName);
					} catch (IOException e) {
						return ResponseEntity.badRequest()
								.body(Arrays.asList("Video upload failed. Please try again later"));
					}
				} // loop end
				if (adminService.courseNameExist(addCourse.getCourseName())) {
					return ResponseEntity.badRequest().body(Arrays.asList("Course name is already exist"));
				} else {
					admin.setCourseId(UUID.randomUUID().toString());
					admin.setAvailable(true);
					admin.setCourseName(addCourse.getCourseName());
					admin.setCourseVideos(uploadFiles);
					admin.setCoursePrice(addCourse.getCoursePrice());
					admin.setShortDescription(addCourse.getShortDescription());
					admin.setLongDescription(addCourse.getLongDescription());
					for(String video: uploadFiles) {
						System.out.println("videos: "+video);
					}
					if (adminService.saveCourse(admin)) {
						return ResponseEntity.ok(Arrays.asList("Course add successfully"));
					} else {
						return ResponseEntity.badRequest().body(Arrays.asList("Course add failed. Please try again later"));
					}
				}
			}
		}
	}
}



