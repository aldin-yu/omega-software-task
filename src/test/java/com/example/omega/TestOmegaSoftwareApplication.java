package com.example.omega;

import org.springframework.boot.SpringApplication;

public class TestOmegaSoftwareApplication {

	public static void main (String[] args) {
		SpringApplication.from(OmegaSoftwareApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
