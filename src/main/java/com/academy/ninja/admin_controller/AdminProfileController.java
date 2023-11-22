package com.academy.ninja.admin_controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

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
import org.springframework.web.multipart.MultipartFile;

import com.academy.ninja.entity.UserModel;
import com.academy.ninja.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin-profile")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminProfileController {

	@Autowired
	private UserService userService;

	@Value("${custom.profile.path}")
	private String EXTERNAL_PATH;

	@GetMapping
	public String adminProfile(Model model, HttpServletRequest request) {
		UserModel user = userService.findByEmail(request);
		if (user != null) {
			model.addAttribute("title", "Profile");
			model.addAttribute("adminData", user);
			model.addAttribute("adminProfile", user.getProfile());
			return "admin/profile";
		} else {
			return "redirect:/login";
		}
	}

	@PostMapping()
	public ResponseEntity<String> adminUpdateProfile(@RequestParam("name") String name,
			@RequestParam(value = "image", required = false) MultipartFile file, HttpServletRequest request) {
		UserModel user = userService.findByEmail(request);
		if (file != null) {
			String fileName = file.getOriginalFilename();
			String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
			if (checkFileExtension(fileExtension)) {
				long fileSize = file.getSize() / 1024;
				if (fileSize < 10) {
					return ResponseEntity.badRequest().body("Image size is less than 10kb");
				} else if (fileSize > 100) {
					return ResponseEntity.badRequest().body("Image size is greater than 100kb");
				} else {
					String newFileName = UUID.randomUUID() + "." + fileExtension;
					Path external = Paths.get(EXTERNAL_PATH, newFileName);
					if (user.getProfile() != null) {
						Path FilePathDelete = Paths.get(EXTERNAL_PATH, user.getProfile());
						if (Files.exists(FilePathDelete)) {
							try {
								Files.delete(FilePathDelete);
								Files.write(external, file.getBytes());
								user.setProfile(newFileName);
								user.setName(name);
								if (userService.saveData(user)) {
									return ResponseEntity.ok("Profile update successfully");
								} else {
									return ResponseEntity.badRequest()
											.body("Profile update failed. Please try again later");
								}
							} catch (IOException e) {
								return ResponseEntity.badRequest().body(e.getMessage().toString());
							}

						} else {
							return ResponseEntity.badRequest().body("Something went wrong. Please try again later");
						}
					} else {
						try {
							Files.write(external, file.getBytes());
							user.setName(name);
							user.setProfile(newFileName);
							if (userService.saveData(user)) {
								return ResponseEntity.ok("Profile update successfully");
							} else {
								return ResponseEntity.badRequest()
										.body("Profile update failed. Please try again later");
							}
						} catch (IOException e) {
							return ResponseEntity.badRequest().body(e.getMessage().toString());
						}
					}
				}
			} else {
				return ResponseEntity.badRequest().body("Please upload image format only");
			}
		} else {
			user.setName(name);
			if (userService.saveData(user)) {
				return ResponseEntity.ok("Profile update successfully");
			} else {
				return ResponseEntity.badRequest().body("Profile update failed. Please try again later");
			}
		}
	}

	private boolean checkFileExtension(String fileExtension) {
		final String[] allowedExtension = { "jpg", "jpeg", "png" };
		for (String extension : allowedExtension) {
			if (fileExtension.equalsIgnoreCase(extension)) {
				return true;
			}
		}
		return false;
	}

}
