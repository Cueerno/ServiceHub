package com.radiuk.innotter_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class InnotterServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InnotterServiceApplication.class, args);
	}

}
