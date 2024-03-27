package nl.kringlooptilburg.productservice.services;

import nl.kringlooptilburg.productservice.domain.entities.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductEntity createProduct(ProductEntity productEntity);

    List<ProductEntity> findAll();

    Optional<ProductEntity> findOne(Integer productId);

    void delete(Integer productId);

    List<ProductEntity> findAllByCategory(String category);

    List<ProductEntity> findAllByPriceBetween(Double minPrice, Double maxPrice);

    List<ProductEntity> priceLessThan(Double price);

    List<ProductEntity> findAllByBrand(String brand);

    List<ProductEntity> findAllByColor(String color);

    List<ProductEntity> findAllBySize(String size);

    List<ProductEntity> findAllByMaterial(String material);

    List<ProductEntity> findAllByProductCondition(String productCondition);

    List<ProductEntity> findAllByAudience(String audience);
}
