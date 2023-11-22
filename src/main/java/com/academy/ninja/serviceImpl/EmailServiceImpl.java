package com.academy.ninja.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.academy.ninja.service.EmailService;

@Service
@Lazy
public class EmailServiceImpl implements EmailService{

	@Autowired
	private final JavaMailSender mailSender;
	
	public EmailServiceImpl(JavaMailSender mailSender) {
		this.mailSender=mailSender;
	}
	
	@Override
	public boolean sendMail(String to, String subject, String text) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(to);
			message.setSubject(subject);
			message.setText(text);
			mailSender.send(message);
			return true;
		}catch(Exception e) {
			return false;
		}
	}
}
