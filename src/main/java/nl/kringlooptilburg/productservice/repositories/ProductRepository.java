package nl.kringlooptilburg.productservice.repositories;

import nl.kringlooptilburg.productservice.domain.entities.ProductEntity;
import nl.kringlooptilburg.productservice.domain.entities.enums.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, Integer>{
    Iterable<ProductEntity> findAllByCategory(String category);
    Iterable<ProductEntity> findAllByPriceBetween(Double minPrice, Double maxPrice);
    Iterable<ProductEntity> priceLessThan(Double price);
    Iterable<ProductEntity> findAllByBrand(Brand brand);
    Iterable<ProductEntity> findAllByColor(Color color);
    Iterable<ProductEntity> findAllBySize(String size);
    Iterable<ProductEntity> findAllByMaterial(Material material);
    Iterable<ProductEntity> findAllByProductCondition(ProductCondition productCondition);
    Iterable<ProductEntity> findAllByAudience(Audience audience);
}
