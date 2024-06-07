package dev.honoreandreas.weatherv2.weathertype;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class TypeService {

    private final TypeRepository typeRepository;

    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public Optional<Type> typeById(Long id) {
        return typeRepository.findById(id);
    }

    public Optional<Type> typeByApiId(int apiId) {
        return typeRepository.findByApiId(apiId);
    }

    @Transactional
    public Type newType(int apiId, String name, String iconCode) {
        return typeRepository.save(new Type(apiId, name, iconCode));
    }
}
