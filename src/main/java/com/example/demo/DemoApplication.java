package com.example.demo;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${other.username}")
    private String other;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @PostConstruct
	private void postConstruct() {
		System.out.println("##########################");
		System.out.println("username: " + username);
		System.out.println("other: " + other);
		System.out.println("##########################");
	}

}
