package dev.honoreandreas.weatherv2;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "locationId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Weather> weathers;

    private String name;
    private double latitude;
    private double longitude;

    public Location(String location, double latitude, double longitude) {

    }
}
