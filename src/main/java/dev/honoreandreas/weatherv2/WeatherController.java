package dev.honoreandreas.weatherv2;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/weather")
@CrossOrigin(origins="*")
@AllArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    public ResponseEntity<Optional<Weather>> getLatestWeather() {
        return new ResponseEntity<>(weatherService.latestWeather(), HttpStatus.OK);
    }

    @GetMapping("/{date}")
    public ResponseEntity<Optional<List<Weather>>> getWeatherByDate(@PathVariable String date) {
        return new ResponseEntity<>(weatherService.allWeathersOnDate(date), HttpStatus.OK);
    }

    @GetMapping("/{startDate}/{endDate}")
    public ResponseEntity<Optional<List<Weather>>> getWeatherRecordsBetweenDates(
            @PathVariable String startDate,
            @PathVariable String endDate
    ) {
        return new ResponseEntity<>(weatherService.allWeathersBetweenDates(startDate, endDate), HttpStatus.OK);
    }

    @GetMapping("/historik")
    public ResponseEntity<Optional<List<Weather>>> getAllWeathers() {
        return new ResponseEntity<>(weatherService.allWeathers(), HttpStatus.OK);
    }




}
