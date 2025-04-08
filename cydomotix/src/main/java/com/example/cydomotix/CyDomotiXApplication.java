package com.example.cydomotix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // pour activer le scheduling des backup de BDD
public class CyDomotiXApplication {

	public static void main(String[] args) {
		SpringApplication.run(CyDomotiXApplication.class, args);
	}

}
