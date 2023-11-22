package com.academy.ninja.serviceImpl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.academy.ninja.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Service
@Lazy
public class PaymentServiceImpl implements PaymentService{
	
	@Value("${razorpay.api.key}")
	private String apiKey;
	
	@Value("${razorpay.api.secret}")
	private String apiSecret;

	@Override
	public String createOrder(int amount, String currency) {
		RazorpayClient razorpay;
		try {
			razorpay = new RazorpayClient(apiKey,apiSecret);
			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", amount*100);
			orderRequest.put("currency", currency);
			orderRequest.put("receipt", "order_rcptid"+System.currentTimeMillis());
			orderRequest.put("payment_capture", 1);
			try {
				Order order = razorpay.orders.create(orderRequest);
				return order.get("id");
			} catch (RazorpayException e) {
				System.out.println("error1: "+e.getMessage().toString());
				e.printStackTrace();
			}
		} catch (RazorpayException e) {
			System.out.println("error2: "+e.getMessage().toString());
			e.printStackTrace();
		}
		return null;
	}

}
