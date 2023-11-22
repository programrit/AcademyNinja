package com.academy.ninja.service;


public interface EmailService {
	
	public boolean sendMail(String to, String subject, String text);
}
