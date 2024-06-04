package nl.kringlooptilburg.productservice.services.impl;

import nl.kringlooptilburg.productservice.domain.entities.ColorEntity;
import nl.kringlooptilburg.productservice.domain.entities.enums.Color;
import nl.kringlooptilburg.productservice.repositories.ColorRepository;
import nl.kringlooptilburg.productservice.services.ColorService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ColorServiceImpl implements ColorService {
    private final ColorRepository colorRepository;

    public ColorServiceImpl(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    @Override
    public ColorEntity findOneByColor(Color color) {
        return colorRepository.findByColor(color);
    }

    @Override
    public Optional<ColorEntity> findById(Integer colorId) {
        return colorRepository.findById(colorId);
    }
}
