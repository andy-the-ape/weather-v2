package dev.honoreandreas.weatherv2;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {
    Optional<List<Weather>> findByDate(String date);

    @Query("SELECT w FROM weather w WHERE (w.date, w.time) = (SELECT MAX(w2.date), MAX(w2.time) FROM weather w2)")
    Optional<Weather> findLatestWeather();

    Optional<List<Weather>> findWeatherByDateGreaterThanEqualAndDateLessThanEqual(String startDate, String endDate, Sort sort);
}
