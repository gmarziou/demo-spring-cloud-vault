package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;

import static org.springframework.util.Assert.isTrue;

@SpringBootApplication
public class DemoApplication {

    @Value("${spring.profiles.active:#{null}}")
    private String profiles;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${other.username}")
    private String other;

    @Autowired
    private DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @PostConstruct
	private void postConstruct() throws SQLException {
		System.out.println("##########################");
        System.out.println("profile(s): " + profiles);
        System.out.println("username: " + username);
        System.out.println("password: " + password);
		System.out.println("other: " + other);

		dataSource.getConnection();
        System.out.println("Successfully connected to database");
        System.out.println("##########################");

        isTrue (!username.equals("to-be-overwritten-by-vault-value"), "Username " + username);
        isTrue (!password.equals("to-be-overwritten-by-vault-value"), "Password " + password);
    }

}
