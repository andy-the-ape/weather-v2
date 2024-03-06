package dev.honoreandreas.weatherv2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Getter
public class WeatherService {
    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private LocationService locationService;

    private Weather currentWeather;

    public Optional<List<Weather>> allWeatherRecords() {
        return Optional.of(weatherRepository.findAll());
    }
    public Optional<Weather> singleWeatherRecord(String date) {
        return weatherRepository.findByDate(date);
    }
    public Optional<List<Weather>> allWeatherRecordsBetweenDates(String startDate, String endDate) {
        return weatherRepository.findWeatherByDateGreaterThanEqualAndDateLessThanEqual(
                startDate,
                endDate,
                Sort.by(Sort.Direction.ASC, "timestamp")
        );
    }

    public String getApiUrl(double latitude, double longitude) {
        return "https://api.openweathermap.org/data/2.5/weather?lat=" +
                latitude +
                "&lon=" +
                longitude +
                "&appid=2438ec868e96bc0d041dc6fde565f0b6&units=metric&lang=da";
    }

    //This method runs every 5 minutes to gather data from the external weather API
    @Scheduled(cron = "0 */5 * * * *")
    public void fetchWeatherFromExternalApi() {
        //By default, it will get weather for this location
        double latitude = 55.39594;
        double longitude = 10.38831;

        //Currently only getting weather for one location, can be changed to loop all locations in database
        //(but be mindful of allowed API calls)
        if (locationService.locationById(1L).isPresent()) {
            latitude = locationService.locationById(1L).get().getLatitude();
            longitude = locationService.locationById(1L).get().getLongitude();
        }

        //Handling date format
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = LocalDate.now().format(dateFormatter);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                getApiUrl(latitude, longitude),
                String.class);
        String weatherJson = responseEntity.getBody();
        try {
            JsonNode weatherNode = new ObjectMapper().readTree(weatherJson);

            currentWeather = createWeather(
                    "Odense",
                    formattedDate,
                    weatherNode.get("weather").get(0).get("id").intValue(),
                    weatherNode.get("weather").get(0).get("main").textValue(),
                    weatherNode.get("weather").get(0).get("description").textValue(),
                    weatherNode.get("weather").get(0).get("icon").textValue(),
                    weatherNode.get("main").get("temp").doubleValue(),
                    weatherNode.get("main").get("humidity").doubleValue(),
                    weatherNode.get("wind").get("speed").doubleValue(),
                    weatherNode.get("wind").get("deg").doubleValue()
            );

        } catch (JsonProcessingException e) {
            e.printStackTrace(System.out);
        }
    }

    public Weather saveWeather(
            String location,
            String date,
            int weatherId,
            String weatherTitle,
            String weatherDescription,
            String weatherIconId,
            double temperature,
            double humidity,
            double windSpeed,
            double windDirection
    ) {
        weatherRepository.save(Weather)
    }

}
