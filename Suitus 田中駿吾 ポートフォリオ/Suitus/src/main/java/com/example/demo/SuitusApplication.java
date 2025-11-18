package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SuitusApplication {

	public static void main(String[] args) {
		SpringApplication.run(SuitusApplication.class, args);
		System.out.println("localhost:8080/でアクセス");
	}

}
