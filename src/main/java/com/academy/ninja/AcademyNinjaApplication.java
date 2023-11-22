package com.academy.ninja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.academy.ninja")
public class AcademyNinjaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcademyNinjaApplication.class, args);
	}

}
