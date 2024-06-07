package dev.honoreandreas.weatherv2.weather;

import dev.honoreandreas.weatherv2.location.Location;
import dev.honoreandreas.weatherv2.weathertype.Type;
import jakarta.persistence.*;

@Entity(name = "weather")
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
    private String description;

    @Column(name = "wind_speed")
    private double windSpeed;

    @Column(name = "wind_direction")
    private int windDirection;

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

    //Default constructor for JPA
    protected Weather() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(int windDirection) {
        this.windDirection = windDirection;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "id=" + id +
                ", location=" + location.getName() +
                ", type=" + type.getName() +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", windSpeed=" + windSpeed +
                ", windDirection=" + windDirection +
                ", description='" + description + '\'' +
                '}';
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
