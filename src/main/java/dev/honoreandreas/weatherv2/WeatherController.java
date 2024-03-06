package dev.honoreandreas.weatherv2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/weather")
@CrossOrigin(origins="*")
public class WeatherController {
    @Autowired
    private WeatherService weatherService;
}
