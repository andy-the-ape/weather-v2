package dev.honoreandreas.weatherv2.weathertype;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.honoreandreas.weatherv2.weather.Weather;
import jakarta.persistence.*;
import java.util.List;

@Entity(name = "type")
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
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

    //Default constructor for JPA
    protected Type() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Weather> getWeathers() {
        return weathers;
    }

    public void setWeathers(List<Weather> weathers) {
        this.weathers = weathers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getApiId() {
        return apiId;
    }

    public void setApiId(int apiId) {
        this.apiId = apiId;
    }

    public String getIconCode() {
        return iconCode;
    }

    public void setIconCode(String iconCode) {
        this.iconCode = iconCode;
    }

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", apiId=" + apiId +
                ", iconCode='" + iconCode + '\'' +
                '}';
    }
}
