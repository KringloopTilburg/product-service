package nl.kringlooptilburg.productservice.config;

import nl.kringlooptilburg.productservice.domain.entities.ColorEntity;
import nl.kringlooptilburg.productservice.domain.entities.enums.Color;
import nl.kringlooptilburg.productservice.repositories.ColorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PopulateDatabaseConfig {

    // Populate the database with the colors, adding a new color is currently not possible
    @Bean
    public CommandLineRunner populateColorRepository(ColorRepository colorRepository) {
        return args -> {
            if (colorRepository.count() == 0) {
                for (Color color : Color.values()) {
                    ColorEntity colorEntity = new ColorEntity();
                    colorEntity.setColor(color);
                    colorRepository.save(colorEntity);
                }
            }
        };
    }
}
