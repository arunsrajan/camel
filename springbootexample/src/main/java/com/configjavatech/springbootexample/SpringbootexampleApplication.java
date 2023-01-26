package com.configjavatech.springbootexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(ignoreResourceNotFound = false, value = {"classpath:application.properties"})
public class SpringbootexampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootexampleApplication.class, args);
	}

}
