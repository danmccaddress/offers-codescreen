package uk.co.mccaddress.offers;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;

@EnableAutoConfiguration
@SpringBootApplication
public class Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}