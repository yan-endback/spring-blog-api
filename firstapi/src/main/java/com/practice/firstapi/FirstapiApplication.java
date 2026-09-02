package com.practice.firstapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class FirstapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirstapiApplication.class, args);
	}

	@RestController
	public class HelloController {

		@GetMapping ("/hello")
		public String hello(@RequestParam(defaultValue = "гость")String name ){
			return "Привет, " + name + "!";
		}
		@GetMapping("/hello/{name}")
		public String helloPath(@PathVariable String name){
			return "Привет из path, " + name + "!";
		}
	}

	@Bean
	CommandLineRunner init(AuthorRepository repository){
		return args -> {
			if(repository.count() == 0){
				repository.save(new Author("Мурат"));
				repository.save(new Author("Айгуль"));
			}
		};
	}
}
