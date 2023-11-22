package com.academy.ninja.custom_configuration;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.academy.ninja.entity.UserModel;
import com.academy.ninja.repository.DBRepository;

@Component
public class CustomUserDetailService implements UserDetailsService{
	
	@Autowired
	private DBRepository db;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserModel user = db.findByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException("User not found");
		}else {
			return new CustomUserDetails(user);
		}
		
	}
	

//	private boolean sendMail(UserModel user) {
//		String token = UUID.randomUUID().toString();
//		user.setExpiredTime(System.currentTimeMillis()+(24*60*60*1000));
//		user.setVerificationToken(token);
//		String link = "http://localhost:8080/verify?token="+token;
//		String text = "Hello "+user.getName()+"\n\n"
//				+ "Your verification link is here "+link
//				+"\n\n"
//				+"Link will be expired after 24hours";
//		if(userService.sendMail(user.getEmail(), "Email Verification", text)) {
//			db.save(user);
//			return true;
//		}else {
//			return false;
//		}
//	}


}
