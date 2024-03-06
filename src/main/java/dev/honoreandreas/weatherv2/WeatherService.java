package dev.honoreandreas.weatherv2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Getter
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherRepository weatherRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    private final LocationService locationService;

    private final TypeService typeService;

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
        Long locationId = 1L;
        double latitude = 55.39594;
        double longitude = 10.38831;
        Location locationObject;

        //Currently only getting weather for one location, can be changed to loop all locations in database
        //(but be mindful of allowed API calls)
        if (locationService.locationById(locationId).isPresent()) {
            locationObject = locationService.locationById(locationId).get();
            latitude = locationObject.getLatitude();
            longitude = locationObject.getLongitude();
        }

        //Handling date and time format
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = LocalDate.now().format(dateFormatter);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = LocalTime.now().format(timeFormatter);

        //Get JSON from external API and map the fields we want
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                getApiUrl(latitude, longitude),
                String.class);
        String weatherJson = responseEntity.getBody();
        try {
            JsonNode weatherNode = new ObjectMapper().readTree(weatherJson);

            int apiId = weatherNode.get("weather").get(0).get("id").intValue(); //Can be used to distinguish similar types
            String typeName = weatherNode.get("weather").get(0).get("main").textValue(); //Weather type E.g. "Regn"
            String weatherDescription = weatherNode.get("weather").get(0).get("description").textValue(); //E.g. "moderat regn"
            String iconCode = weatherNode.get("weather").get(0).get("icon").textValue(); //Can be used to set our own icon
            double temperature = weatherNode.get("main").get("temp").doubleValue(); //comes out in celcius with 2 decimals
            int humidity = weatherNode.get("main").get("humidity").intValue(); //comes as integer from 0-100 (%)
            double windSpeed = weatherNode.get("wind").get("speed").doubleValue(); //comes out in km/t with 2 decimals
            int windDirection = weatherNode.get("wind").get("deg").intValue(); //comes out in degrees as int from 0-360

            //Getting the necessary Type Object (if it exists), otherwise create it
            Type typeObject;
            if (typeService.typeByApiId(apiId).isPresent()) {
                typeObject = typeService.typeByApiId(apiId).get();
            } else {
                typeObject = typeService.newType(apiId, typeName, iconCode);
            }

            //Now create and persist the Weather Object
            weatherRepository.save(newWeather(
                    locationObject,
                    typeObject,
                    formattedDate,
                    formattedTime,
                    temperature,
                    humidity,
                    windSpeed,
                    windDirection,
                    weatherDescription
            ));

        } catch (JsonProcessingException e) {
            e.printStackTrace(System.out);
        }
    }

    public Weather newWeather(
                Location location,
                Type type,
                String date,
                String time,
                double temperature,
                int humidity,
                double windSpeed,
                int windDirection,
                String description
    ) {
        return new Weather(
                location,
                type,
                date,
                time,
                temperature,
                humidity,
                windSpeed,
                windDirection,
                description
        );
    }

}
