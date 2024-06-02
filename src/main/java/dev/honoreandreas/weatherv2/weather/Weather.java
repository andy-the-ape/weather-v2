package dev.honoreandreas.weatherv2.weather;

import dev.honoreandreas.weatherv2.location.Location;
import dev.honoreandreas.weatherv2.weathertype.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "weather")
@Getter
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne()
    @JoinColumn(name = "type_id")
    private Type type;

    private String date;
    private String time;
    private double temperature;
    private int humidity;

    @Column(name = "wind_speed")
    private double windSpeed;

    @Column(name = "wind_direction")
    private int windDirection;

    private String description;

    public Weather(
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
        this.location = location;
        this.type = type;
        this.date = date;
        this.time = time;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.description = description;
    }
}
