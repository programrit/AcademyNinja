package com.academy.ninja.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.academy.ninja.entity.ContactModel;

public interface UserContactRepository extends JpaRepository<ContactModel,Integer>{

}
