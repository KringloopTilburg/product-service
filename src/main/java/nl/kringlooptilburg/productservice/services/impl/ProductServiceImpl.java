package nl.kringlooptilburg.productservice.services.impl;

import nl.kringlooptilburg.productservice.domain.entities.ProductEntity;
import nl.kringlooptilburg.productservice.repositories.ProductRepository;
import nl.kringlooptilburg.productservice.services.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductEntity createProduct(ProductEntity productEntity) {
        return productRepository.save(productEntity);
    }

    @Override
    public List<ProductEntity> findAll() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Optional<ProductEntity> findOne(Integer productId) {
        return productRepository.findById(productId);
    }

    @Override
    public void delete(Integer productId) {
        productRepository.deleteById(productId);
    }
}
