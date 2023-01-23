package com.configjavatech.springbootexample;

import java.util.Random;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component("rand")
public class RandomGenerator {
	
	@Bean
	public Random random() {
		return new Random(System.currentTimeMillis());
	}
	
}
