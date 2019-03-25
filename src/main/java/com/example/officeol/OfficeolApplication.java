package com.example.officeol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAutoConfiguration
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.officeol.repository")
public class OfficeolApplication {

	public static void main(String[] args) {
		SpringApplication.run(OfficeolApplication.class, args);
	}

}

