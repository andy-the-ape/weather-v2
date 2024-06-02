package dev.honoreandreas.weatherv2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableScheduling
@EnableJpaRepositories
public class SpringConfig {

    @Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
