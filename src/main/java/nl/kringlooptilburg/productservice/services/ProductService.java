package nl.kringlooptilburg.productservice.services;

import nl.kringlooptilburg.productservice.domain.entities.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductEntity createProduct(ProductEntity productEntity);

    List<ProductEntity> findAll();

    Optional<ProductEntity> findOne(Integer productId);

    void delete(Integer productId);
}
