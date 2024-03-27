package nl.kringlooptilburg.productservice.services.impl;

import nl.kringlooptilburg.productservice.domain.entities.ProductEntity;
import nl.kringlooptilburg.productservice.domain.entities.enums.*;
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

    @Override
    public List<ProductEntity> findAllByCategory(String category) {
        return StreamSupport.stream(productRepository.findAllByCategory(category).spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public List<ProductEntity> findAllByPriceBetween(Double minPrice, Double maxPrice) {
        return StreamSupport.stream(productRepository.findAllByPriceBetween(minPrice, maxPrice).spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public List<ProductEntity> priceLessThan(Double price) {
        return StreamSupport.stream(productRepository.priceLessThan(price).spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public List<ProductEntity> findAllByBrand(String brand) {
        Brand brandEnum = Brand.valueOf(brand);
        return StreamSupport.stream(productRepository.findAllByBrand(brandEnum).spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public List<ProductEntity> findAllByColor(String color) {
        Color colorEnum = Color.valueOf(color);
        return StreamSupport.stream(productRepository.findAllByColor(colorEnum).spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public List<ProductEntity> findAllBySize(String size) {
        return StreamSupport.stream(productRepository.findAllBySize(size).spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public List<ProductEntity> findAllByMaterial(String material) {
        Material materialEnum = Material.valueOf(material);
        return StreamSupport.stream(productRepository.findAllByMaterial(materialEnum).spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public List<ProductEntity> findAllByProductCondition(String productCondition) {
        ProductCondition productConditionEnum = ProductCondition.valueOf(productCondition);
        return StreamSupport.stream(productRepository.findAllByProductCondition(productConditionEnum).spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public List<ProductEntity> findAllByAudience(String audience) {
        Audience audienceEnum = Audience.valueOf(audience);
        return StreamSupport.stream(productRepository.findAllByAudience(audienceEnum).spliterator(), false).collect(Collectors.toList());
    }
}
