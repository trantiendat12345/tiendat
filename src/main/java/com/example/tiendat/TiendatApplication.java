package com.example.tiendat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TiendatApplication {

	public static void main(String[] args) {
		SpringApplication.run(TiendatApplication.class, args);
	}

}
