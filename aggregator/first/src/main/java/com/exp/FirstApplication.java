package com.exp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FirstApplication {

	private final Logger log = LoggerFactory.getLogger(FirstApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(FirstApplication.class, args);
	}

	@Bean
	public ApplicationRunner init() {
		return args -> {
			log.info("{}", Test.test());
		};
	}

}
