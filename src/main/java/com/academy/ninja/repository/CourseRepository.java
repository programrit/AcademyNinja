package com.academy.ninja.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.academy.ninja.entity.AdminModel;

public interface CourseRepository extends JpaRepository<AdminModel,Integer>{
		AdminModel findByCourseName(String courseName);
		AdminModel findByCourseId(String courseId);
}
