package dev.honoreandreas.weatherv2.location;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public String getStandardLocationName() {
        return standardLocationName;
    }

    public double getStandardLocationLatitude() {
        return standardLocationLatitude;
    }

    public double getStandardLocationLongitude() {
        return standardLocationLongitude;
    }

    @Value("${location.name}")
    private String standardLocationName;
    @Value("${location.latitude}")
    private double standardLocationLatitude;
    @Value("${location.longitude}")
    private double standardLocationLongitude;

    public Optional<Location> locationById(Long id) {
        return locationRepository.findById(id);
    }

    public Optional<Location> firstLocation() {
        return locationRepository.findFirstByOrderByIdAsc();
    }

    @Transactional
    public Location newLocation(String name, double latitude, double longitude) {
        Location locationObject = Location.getInstance(name, latitude, longitude);
        return locationRepository.save(locationObject);
    }
}
