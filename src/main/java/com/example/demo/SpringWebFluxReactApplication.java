package com.example.demo;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

@SpringBootApplication
@Log4j2
@RestController
public class SpringWebFluxReactApplication {
	@Autowired
	ProfileRepository profileRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringWebFluxReactApplication.class, args);
	}

	@RequestMapping("hi")
	public Flux<Profile> all() {
		log.info("--hi--");
		return this.profileRepository.findAll();
	}

	@Bean
	ApplicationRunner init1() {
		return v -> {
			profileRepository.deleteAll() // <4>
					.thenMany(Flux.just("A", "B", "C", "D") // <5>
							.map(name1 -> new Profile(UUID.randomUUID().toString(), name1 + "@email.com")) // <6>
							.flatMap(profileRepository::save) // <7>
			).thenMany(profileRepository.findAll()) // <8>
					.subscribe(profile1 -> log.info("saving " + profile1.toString())); // <9>
		};
	}

}
