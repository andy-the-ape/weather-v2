package dev.honoreandreas.weatherv2;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Location newLocation(String location, double latitude, double longitude) {
        return locationRepository.save(new Location(location, latitude, longitude));
    }
}
