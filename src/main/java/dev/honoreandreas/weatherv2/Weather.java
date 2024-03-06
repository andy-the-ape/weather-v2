package dev.honoreandreas.weatherv2;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "weather")
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "location_id")
    private Location locationId;

    @ManyToOne()
    @JoinColumn(name = "type_id")
    private Type typeId;

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
                   Location locationId,
                   Type typeId,
                   String date,
                   String time,
                   double temperature,
                   int humidity,
                   double windSpeed,
                   int windDirection,
                   String description
    ) {
        this.locationId = locationId;
        this.typeId = typeId;
        this.date = date;
        this.time = time;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.description = description;
    }
}
