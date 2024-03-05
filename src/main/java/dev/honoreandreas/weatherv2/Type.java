package dev.honoreandreas.weatherv2;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "type")
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "weather", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Weather> weathers = new HashSet<>();

    private String name;
    private String icon;
}
