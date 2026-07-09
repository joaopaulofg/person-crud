package com.joaopaulofg.personcrud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.joaopaulofg.personcrud.config.AppSecurityProperties;


@SpringBootApplication
@EnableConfigurationProperties(AppSecurityProperties.class)
public class PersonCrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersonCrudApplication.class, args);
	}

}
