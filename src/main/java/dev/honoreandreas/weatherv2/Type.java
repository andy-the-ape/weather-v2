package dev.honoreandreas.weatherv2;

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

    @OneToMany(mappedBy = "typeId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
