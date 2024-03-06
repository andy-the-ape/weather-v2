package dev.honoreandreas.weatherv2;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TypeService {

    private final TypeRepository typeRepository;

    public Optional<Type> typeById(Long id) {
        return typeRepository.findById(id);
    }

    public Optional<Type> typeByApiId(int apiId) {
        return typeRepository.findByApiId(apiId);
    }

    public Type newType(int apiId, String name, String iconCode) {
        return typeRepository.save(new Type(apiId, name, iconCode));
    }
}
