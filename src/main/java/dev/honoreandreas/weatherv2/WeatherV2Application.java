package dev.honoreandreas.weatherv2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class WeatherV2Application {

	public static void main(String[] args) {
		SpringApplication.run(WeatherV2Application.class, args);
	}

}
