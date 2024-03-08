package dev.honoreandreas.weatherv2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "type")
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Weather> weathers;

    private String name;

    @Column(name = "api_id")
    private int apiId;

    @Column(name = "icon_code")
    private String iconCode;

    public Type(int apiId, String name, String iconCode) {
        this.apiId = apiId;
        this.name = name;
        this.iconCode = iconCode;
    }
}
