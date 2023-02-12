package com.chung.receiptsmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class ReceiptsManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReceiptsManagerApplication.class, args);
	}

}
