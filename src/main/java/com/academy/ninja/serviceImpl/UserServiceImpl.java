package com.academy.ninja.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.academy.ninja.entity.AdminModel;
import com.academy.ninja.entity.ContactModel;
import com.academy.ninja.entity.CourseModel;
import com.academy.ninja.entity.UserModel;
import com.academy.ninja.repository.DBRepository;
import com.academy.ninja.repository.OrderRepository;
import com.academy.ninja.repository.UserContactRepository;
import com.academy.ninja.service.JwtService;
import com.academy.ninja.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private final DBRepository db;
	@Autowired
	private final BCryptPasswordEncoder encoder;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private OrderRepository order;
	@Autowired
	private final UserContactRepository userContact;
	
	
	
	public UserServiceImpl(DBRepository db, BCryptPasswordEncoder encoder,OrderRepository order,UserContactRepository userContact) {
		this.db = db;
		this.encoder=encoder;
		this.order=order;
		this.userContact = userContact;
	}

	@Override
	public boolean emailExist(String email) {
		UserModel user =  db.findByEmail(email);
		return user !=null;
	}

	@Override
	public UserModel createUser(UserModel user) {
		String hashPassword = encoder.encode(user.getPassword());
		user.setPassword(hashPassword);
		user.setRole("ROLE_USER");
		return db.save(user);
	}
	

	@Override
	public UserModel findVerificationToken(String verificationToken) {
		UserModel user = db.findByVerificationToken(verificationToken);
		if(user!=null) {
			return user;
		}
		return null;
	}

	@Override
	public boolean saveData(UserModel user) {
		UserModel users = db.save(user);
		if(users !=null) {
			return true;
		}
		return false;
	}

	@Override
	public UserModel findByEmail(HttpServletRequest request) {
		String getToken = getRefreshToken(request);
		if(getToken !=null) {
			String email = jwtService.extractUsername(getToken);
			if(email!=null) {
				UserModel user = db.findByEmail(email);
				if(user!=null) {
					return user;
				}
			}
		}
		return null;
	}
	
	private String getRefreshToken(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if(cookies !=null) {
			for(Cookie cookie: cookies) {
				if(cookie.getName().equals("refreshToken")) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	@Override
	public boolean updatePassword(String oldPassword, String newPassword,UserModel user) {
		if(user!=null) {
			if(encoder.matches(oldPassword, user.getPassword())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean deleteCookieAndRememberMe(HttpServletRequest request,HttpServletResponse response) {
		request.getSession().removeAttribute("Authorization");
		if(clearCookie(request,response)) {
			if(clearRememberMe(request,response)) {
				return true;
			}
		}
		return false;
	}
	
	
	private boolean clearRememberMe(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies= request.getCookies();
		if(cookies!=null) {
			for(Cookie cookie: cookies) {
				if(cookie.getName().equals("remember-me")) {
					cookie.setMaxAge(0);
					response.addCookie(cookie);
					return true;
				}
			}
		}
		return false;
		
	}

	private boolean clearCookie(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies= request.getCookies();
		if(cookies!=null) {
			for(Cookie cookie: cookies) {
				if(cookie.getName().equals("refreshToken")) {
					cookie.setMaxAge(0);
					response.addCookie(cookie);
					return true;
				}
			}
		}
		return false;
		
	}

	@Override
	public UserModel findByGmail(String email) {
		UserModel user = db.findByEmail(email);
		if(user!=null) {
			return user;
		}
		return null;
	}

	@Override
	public String passwordEncoder(String password) {
		if(password!=null) {
			return encoder.encode(password);
		}
		return null;
	}

	@Override
	public boolean CheckPassword(String password, UserModel user) {
		if(encoder.matches(password, user.getPassword())) {
			return true;
		}
		return false;
	}

	@Override
	public UserModel addAdmin(UserModel user) {
		String hashPassword = encoder.encode(user.getPassword());
		user.setPassword(hashPassword);
		user.setRole("ROLE_ADMIN");
		return db.save(user);
	}

	@Override
	public CourseModel checkOrderId(String orderId) {
		CourseModel check = order.findByOrderId(orderId);
		if(check!=null) {
			return check;
		}
		return null;
	}

	@Override
	public boolean saveOrder(CourseModel course) {
		CourseModel status = order.save(course);
		if(status !=null) {
			return true;
		}
		return false;
	}

	@Override
	public List<CourseModel> findAllMyCourse(UserModel user) {
		return order.findByUser(user);
	}

	@Override
	public CourseModel existUserCourse(AdminModel admin, UserModel user) {
		CourseModel course = order.findByParams(admin, user);
		if(course!=null) {
			return course;
		}
		return null;
	}
	
	
	@Override
	public boolean saveContact(ContactModel contact) {
		ContactModel contactModel = userContact.save(contact);
		if(contactModel !=null) {
			return true;
		}
		return false;
	}
	

	

}
