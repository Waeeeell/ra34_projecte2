package com.ra34.projecte2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Aplicación principal de Spring Boot.
 * API REST para gestión de productos de tienda online.
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.ra34.projecte2")
public class Projecte2Application {

	public static void main(String[] args) {
		SpringApplication.run(Projecte2Application.class, args);
	}

}
