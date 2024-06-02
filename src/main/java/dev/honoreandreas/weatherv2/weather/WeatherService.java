package dev.honoreandreas.weatherv2.weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.honoreandreas.weatherv2.ApiKeyConfig;
import dev.honoreandreas.weatherv2.location.Location;
import dev.honoreandreas.weatherv2.location.LocationService;
import dev.honoreandreas.weatherv2.weathertype.Type;
import dev.honoreandreas.weatherv2.weathertype.TypeService;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Getter
@RequiredArgsConstructor
@Transactional
public class WeatherService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherService.class);
    private static final String API_BASE_URL = "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s&units=metric&lang=da";

    private final WeatherRepository weatherRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final LocationService locationService;
    private final TypeService typeService;
    private final ApiKeyConfig apiKeyConfig;

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
        Optional<List<Weather>> allWeathersTheLast48Hours = allWeathersBetweenDates(
                currentDate.minusDays(2).toString(),
                currentDate.toString()
        );

        if (allWeathersTheLast48Hours.isPresent()) {
            List<Weather> allWeathersUnwrappedList = allWeathersTheLast48Hours.get();
            Map<LocalDateTime, Weather> hourlyWeatherMap = new TreeMap<>(); // TreeMap for automatic sorting
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            for (Weather weather : allWeathersUnwrappedList) {
                LocalDateTime dateTime = LocalDateTime.parse(weather.getDate() + " " + weather.getTime(), formatter);
                int hour = dateTime.getHour();
                LocalDateTime mapKey = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(), hour, 0);

                // Check if there's already a Weather object for this hour
                Weather existingWeather = hourlyWeatherMap.get(mapKey);
                if (existingWeather == null || existingWeather.getDate().compareTo(weather.getDate()) > 0) {
                    hourlyWeatherMap.put(mapKey, weather);
                }
            }

            List<Weather> latest48HourlyWeathersList = new ArrayList<>(hourlyWeatherMap.values());
            return Optional.of(latest48HourlyWeathersList);
        } else {
            return Optional.empty();
        }
    }

    private String getApiUrl(double latitude, double longitude) {
        return String.format(API_BASE_URL, latitude, longitude, apiKeyConfig.getApiKey());
    }

    private Location getLocationObject() {
        /* Get the necessary Location Object (if it exists), otherwise create it. By default,
        it will get weather for Syddansk Universitet. Currently only getting weather for one location, can be
        changed to loop all locations in database (but be mindful of allowed API calls) */
        LOGGER.info("Reached getLocationObject method");
        Location locationObject;
        if (locationService.firstLocation().isPresent()) {
            LOGGER.info("Location found, using it");
            locationObject = locationService.firstLocation().get();
        } else {
            LOGGER.info("No location found, creating a new one");
            locationObject = locationService.newLocation("Syddansk Universitet", 55.36821, 10.42395);
        }
        return locationObject;
    }

    private String getFormattedDate() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.now().format(dateFormatter);
    }

    private String getFormattedTime() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalTime.now().format(timeFormatter);
    }

    private String fetchWeatherJson(Location locationObject) {
        LOGGER.info("Reached fetchWeatherJson method");
        //Get JSON from external API
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                getApiUrl(locationObject.getLatitude(), locationObject.getLongitude()),
                String.class);
        return responseEntity.getBody();
    }

    private void processWeatherJson(String weatherJson, Location locationObject, String formattedDate, String formattedTime) throws JsonProcessingException {
        LOGGER.info("Reached processWeatherJson method");
        //Map the fields we want
        JsonNode weatherNode = new ObjectMapper().readTree(weatherJson);
        int apiId = weatherNode.get("weather").get(0).get("id").intValue(); //Can be used to distinguish similar types
        String typeName = weatherNode.get("weather").get(0).get("main").textValue(); //Weather type E.g. "Regn"
        String weatherDescription = weatherNode.get("weather").get(0).get("description").textValue(); //E.g. "moderat regn"
        String iconCode = weatherNode.get("weather").get(0).get("icon").textValue(); //Can be used to set our own icon
        double temperature = weatherNode.get("main").get("temp").doubleValue(); //comes out in Celsius with 2 decimals
        int humidity = weatherNode.get("main").get("humidity").intValue(); //comes as integer from 0-100 (%)
        double windSpeed = weatherNode.get("wind").get("speed").doubleValue(); //comes out in km/t with 2 decimals
        int windDirection = weatherNode.get("wind").get("deg").intValue(); //comes out in degrees as int from 0-360

        Type typeObject = getTypeObject(apiId, typeName, iconCode);

        //Now create and persist the Weather Object, and also store it in the currentWeather variable
        currentWeather = weatherRepository.save(new Weather.Builder()
                .setLocation(locationObject)
                .setType(typeObject)
                .setDate(formattedDate)
                .setTime(formattedTime)
                .setTemperature(temperature)
                .setHumidity(humidity)
                .setWindSpeed(windSpeed)
                .setWindDirection(windDirection)
                .setDescription(weatherDescription)
                .build()
        );
    }

    private Type getTypeObject(int apiId, String typeName, String iconCode) {
        LOGGER.info("Reached getTypeObject method");
        Type typeObject;
        //Get the necessary Type Object (if it exists), otherwise create it
        if (typeService.typeByApiId(apiId).isPresent()) {
            LOGGER.info("a type was found with that api_id");
            typeObject = typeService.typeByApiId(apiId).get();
        } else {
            LOGGER.info("no type was found with that api_id, adding it to the db.");
            typeObject = typeService.newType(apiId, typeName, iconCode);
        }
        return typeObject;
    }

    //This method runs every 5 minutes to gather data from the external weather API
//    @Scheduled(cron = "0 */5 * * * *")
    @Scheduled(fixedDelay = 30000)
    public void fetchWeatherFromExternalApi() {
        LOGGER.info("scheduled method reached");
        Location locationObject = getLocationObject();
        String formattedDate = getFormattedDate();
        String formattedTime = getFormattedTime();

        try {
            String weatherJson = fetchWeatherJson(locationObject);
            processWeatherJson(weatherJson, locationObject, formattedDate, formattedTime);
            System.out.println("current weather is = "+currentWeather);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error processing JSON from external API", e);
        }
    }

}