package dev.honoreandreas.weatherv2.location;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public Optional<Location> locationById(Long id) {
        return locationRepository.findById(id);
    }

    public Optional<Location> firstLocation() {
        return locationRepository.findFirstByOrderByIdAsc();
    }

    @Transactional
    public Location newLocation(String name, double latitude, double longitude) {
        Location locationObject = new Location(name, latitude, longitude);
        return locationRepository.save(locationObject);
    }
}
