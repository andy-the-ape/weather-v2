package dev.honoreandreas.weatherv2.weather;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public ResponseEntity<Optional<Weather>> getLatestWeather() {
        return new ResponseEntity<>(weatherService.latestWeather(), HttpStatus.OK);
    }

    @GetMapping("/history/{date}")
    public ResponseEntity<List<Weather>> getWeatherByDate(@PathVariable String date) {
        return new ResponseEntity<>(weatherService.allWeathersOnDate(date), HttpStatus.OK);
    }

    @GetMapping("/history/{startDate}/{endDate}")
    public ResponseEntity<List<Weather>> getWeatherRecordsBetweenDates(
            @PathVariable String startDate,
            @PathVariable String endDate
    ) {
        return new ResponseEntity<>(weatherService.allWeathersBetweenDates(startDate, endDate), HttpStatus.OK);
    }

    @GetMapping("/history/all")
    public ResponseEntity<List<Weather>> getAllWeathers() {
        return new ResponseEntity<>(weatherService.allWeathers(), HttpStatus.OK);
    }

    @GetMapping("/history/48")
    public ResponseEntity<List<Weather>> getLatest48HourlyWeathers() {
        return new ResponseEntity<>(weatherService.latest48HourlyWeathers(), HttpStatus.OK);
    }

}
