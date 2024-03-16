package dev.honoreandreas.weatherv2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Getter
@RequiredArgsConstructor
@Transactional
public class WeatherService {

    private final WeatherRepository weatherRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    private final LocationService locationService;

    private final TypeService typeService;

    private Weather currentWeather;

    public Optional<Weather> singleWeatherById(Long id) {
        return weatherRepository.findById(id);
    }

    public Optional<Weather> latestWeather() {
        return weatherRepository.findTopDistinctByOrderByDateDescTimeDesc();
    }
    public Optional<List<Weather>> allWeathers() {
        return Optional.of(weatherRepository.findAll());
    }
    public Optional<List<Weather>> allWeathersOnDate(String date) {
        return weatherRepository.findByDate(date);
    }
    public Optional<List<Weather>> allWeathersBetweenDates(String startDate, String endDate) {
        return weatherRepository.findWeatherByDateGreaterThanEqualAndDateLessThanEqual(
                startDate,
                endDate,
                Sort.by(Sort.Direction.ASC, "date","time")
        );
    }
    public Optional<List<Weather>> latest48HourlyWeathers() {
        LocalDate currentDate = LocalDate.now();
        Optional<List<Weather>> allWeathersTheLast48Hours = allWeathersBetweenDates(currentDate.minusDays(2).toString(), currentDate.toString());
        if (allWeathersTheLast48Hours.isPresent()) {
            List<Weather> allWeathersUnwrappedList = allWeathersTheLast48Hours.get();
            Map<String, Weather> hourlyWeatherMap = new HashMap<>();
            for (Weather weather : allWeathersUnwrappedList) {
                String day = weather.getDate().split("-",3)[2];
                String hour = weather.getTime().split(":",3)[0];
                String mapKey = day.concat(hour);
                hourlyWeatherMap.putIfAbsent(mapKey, weather);
            }
            Collection<Weather> values = hourlyWeatherMap.values();
            List<Weather> sortedValues = new ArrayList<>(values);
            sortedValues.sort(Comparator.comparing(String::valueOf));
            List<Weather> latest48HourlyWeathersList = new ArrayList<>(sortedValues);
            return Optional.of(latest48HourlyWeathersList);
        } else {
            return Optional.empty();
        }
    }

    public String getApiUrl(double latitude, double longitude) {
        return "https://api.openweathermap.org/data/2.5/weather?lat=" +
                latitude +
                "&lon=" +
                longitude +
                "&appid=2438ec868e96bc0d041dc6fde565f0b6&units=metric&lang=da";
    }

    //This method runs every 5 minutes to gather data from the external weather API
//    @Scheduled(cron = "0 */5 * * * *")
    @Scheduled(fixedDelay = 30000)
    public void fetchWeatherFromExternalApi() {
        System.out.println("scheduled method reached");
        /* Get the necessary Location Object (if it exists), otherwise create it. By default,
        it will get weather for Odense. Currently only getting weather for one location, can be
        changed to loop all locations in database (but be mindful of allowed API calls) */

        Location locationObject;
        if (locationService.firstLocation().isPresent()) {
            System.out.println("a first location was found");
            locationObject = locationService.firstLocation().get();
        } else {
            System.out.println("no location was found - attempting to add standard location");
            locationObject = locationService.newLocation("Odense", 55.39594, 10.38831);
        }

        //Handling date and time format
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = LocalDate.now().format(dateFormatter);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = LocalTime.now().format(timeFormatter);

        //Get JSON from external API and map the fields we want
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                getApiUrl(locationObject.getLatitude(), locationObject.getLongitude()),
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

            //Get the necessary Type Object (if it exists), otherwise create it
            Type typeObject;
            if (typeService.typeByApiId(apiId).isPresent()) {
                System.out.println("a type was found with that api_id");
                typeObject = typeService.typeByApiId(apiId).get();
            } else {
                System.out.println("no type was found with that api_id, adding it to the db.");
                typeObject = typeService.newType(apiId, typeName, iconCode);
            }

            //Now create and persist the Weather Object, and also store it in the currentWeather variable
            currentWeather = weatherRepository.save(newWeather(
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
            System.out.println("current weather is = "+currentWeather);
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
