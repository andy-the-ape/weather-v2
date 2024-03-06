package dev.honoreandreas.weatherv2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public Optional<Location> locationById(Long id) {
        return locationRepository.findById(id);
    }
}
