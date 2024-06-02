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

    public Weather(Builder builder) {
        this.location = builder.location;
        this.type = builder.type;
        this.date = builder.date;
        this.time = builder.time;
        this.temperature = builder.temperature;
        this.humidity = builder.humidity;
        this.windSpeed = builder.windSpeed;
        this.windDirection = builder.windDirection;
        this.description = builder.description;
    }

    public static class Builder {
        private Location location;
        private Type type;
        private String date;
        private String time;
        private double temperature;
        private int humidity;
        private double windSpeed;
        private int windDirection;
        private String description;

        public Builder setLocation(Location location) {
            this.location = location;
            return this;
        }

        public Builder setType(Type type) {
            this.type = type;
            return this;
        }

        public Builder setDate(String date) {
            this.date = date;
            return this;
        }

        public Builder setTime(String time) {
            this.time = time;
            return this;
        }

        public Builder setTemperature(double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder setHumidity(int humidity) {
            this.humidity = humidity;
            return this;
        }

        public Builder setWindSpeed(double windSpeed) {
            this.windSpeed = windSpeed;
            return this;
        }

        public Builder setWindDirection(int windDirection) {
            this.windDirection = windDirection;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Weather build() {
            return new Weather(this);
        }
    }
}
