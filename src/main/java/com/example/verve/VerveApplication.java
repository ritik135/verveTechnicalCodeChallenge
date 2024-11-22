package com.example.verve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.example.verve.*")
public class VerveApplication {

	public static void main(String[] args) {
		SpringApplication.run(VerveApplication.class, args);
	}

}
