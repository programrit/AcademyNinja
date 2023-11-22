package com.academy.ninja.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.academy.ninja.entity.AdminModel;
import com.academy.ninja.entity.CourseModel;
import com.academy.ninja.entity.UserModel;

public interface OrderRepository extends JpaRepository<CourseModel,Integer>{
	boolean existsByUser(UserModel user);
	@Query("SELECT e FROM CourseModel e WHERE e.admin=?1 AND e.user=?2")
	CourseModel findByParams(AdminModel admin, UserModel user);
	CourseModel findByOrderId(String orderId);
	List<CourseModel> findByUser(UserModel user);
}
