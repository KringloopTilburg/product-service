package nl.kringlooptilburg.productservice.repositories;

import nl.kringlooptilburg.productservice.domain.entities.ColorEntity;
import nl.kringlooptilburg.productservice.domain.entities.enums.Color;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends CrudRepository<ColorEntity, Integer> {
    ColorEntity findByColor(Color color);
    boolean existsByColor(Color color);
}
