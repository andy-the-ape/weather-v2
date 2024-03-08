package dev.honoreandreas.weatherv2;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {
    Optional<List<Weather>> findByDate(String date);
    Optional<List<Weather>> findWeatherByDateGreaterThanEqualAndDateLessThanEqual(String startDate, String endDate, Sort sort);
}
