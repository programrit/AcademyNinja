package com.academy.ninja.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.academy.ninja.entity.UserModel;


public interface DBRepository extends JpaRepository<UserModel, Integer>{
	public UserModel findByEmail(String email);
	public UserModel findByVerificationToken(String verificationToken);
	public long countByRole(String role);
	public UserModel findByUserId(String userId);
}
