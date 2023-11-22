package com.academy.ninja.user_controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.academy.ninja.entity.CourseModel;
import com.academy.ninja.service.UserService;

@RestController
@RequestMapping("/payment")
public class PaymentController {
	
	@Autowired
	private UserService userService;

	@Value("${razorpay.api.key}")
	private String apiKey;
	
	@GetMapping("/key")
	public String rezorpayApiKey() {
		return apiKey;
	}
	
	@PostMapping
	public ResponseEntity<String> paymentStatus(@RequestParam boolean status,@RequestParam String orderId){
		if(orderId ==null || orderId.isEmpty()) {
			return ResponseEntity.badRequest().body("Invalid order id");
		}else {
			CourseModel course = userService.checkOrderId(orderId);
			if(course !=null) {
				course.setStatus(status);
				if(userService.saveOrder(course)) {
					if(status) {
						return ResponseEntity.ok("Payment successfully");
					}else {
						return ResponseEntity.badRequest().body("Payment failed. Amount is return 24 hours");
					}
				}else {
					return ResponseEntity.badRequest().body("Payment status update failed");
				}
			}else {
				return ResponseEntity.badRequest().body("Invalid order id");
			}
		}
	}
}
