package com.example.officeol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class OfficeolApplication {

	public static void main(String[] args) {
		SpringApplication.run(OfficeolApplication.class, args);
	}

}

