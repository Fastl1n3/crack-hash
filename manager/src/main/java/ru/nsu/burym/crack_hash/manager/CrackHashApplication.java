package ru.nsu.burym.crack_hash.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CrackHashApplication {

	public static void main(final String[] args) {
		SpringApplication.run(CrackHashApplication.class, args);
	}
}
