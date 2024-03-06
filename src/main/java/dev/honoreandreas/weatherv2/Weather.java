package dev.honoreandreas.weatherv2;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
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

    private String title;

    private String description;
}
