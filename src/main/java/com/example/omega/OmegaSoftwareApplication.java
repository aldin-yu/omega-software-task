package com.example.omega;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@ConfigurationPropertiesScan  // Automatically scans for @ConfigurationProperties annotated classes
public class OmegaSoftwareApplication {

	public static void main(String[] args) {
		SpringApplication.run(OmegaSoftwareApplication.class, args);
	}

}
