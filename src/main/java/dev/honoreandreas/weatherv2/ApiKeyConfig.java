package dev.honoreandreas.weatherv2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiKeyConfig {
    @Value("${apiKey}")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }
}
