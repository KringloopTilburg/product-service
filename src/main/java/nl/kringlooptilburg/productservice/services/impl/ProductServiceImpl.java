package nl.kringlooptilburg.productservice.services.impl;

import nl.kringlooptilburg.productservice.domain.entities.ColorEntity;
import nl.kringlooptilburg.productservice.domain.entities.ProductEntity;
import nl.kringlooptilburg.productservice.domain.entities.enums.*;
import nl.kringlooptilburg.productservice.repositories.ProductRepository;
import nl.kringlooptilburg.productservice.services.ColorService;
import nl.kringlooptilburg.productservice.services.ProductService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;
    private ColorService colorService;

    public ProductServiceImpl(ProductRepository productRepository, ColorService colorService) {
        this.productRepository = productRepository;
        this.colorService = colorService;
    }

    private void setColorsForProduct(ProductEntity productEntity) {
        Iterable<Integer> colorIds = productRepository.findColorIdsByProductId(productEntity.getProductId());
        Set<ColorEntity> colors = new HashSet<>();
        for (Integer colorId : colorIds) {
            Optional<ColorEntity> colorEntity = colorService.findById(colorId);
            colorEntity.ifPresent(colors::add);
        }
        productEntity.setColors(colors);
    }

    @Override
    public ProductEntity createProduct(ProductEntity productEntity) {
        return productRepository.save(productEntity);
    }

    @Override
    public List<ProductEntity> findAll() {
        List<ProductEntity> products = StreamSupport.stream(productRepository.findAll().spliterator(), false).collect(Collectors.toList());
        products.forEach(this::setColorsForProduct);
        return products;
    }

    @Override
    public Optional<ProductEntity> findOne(Integer productId) {
        Optional<ProductEntity> productEntity = productRepository.findById(productId);
        if (productEntity.isPresent()) {
            Iterable<Integer> colorIds = productRepository.findColorIdsByProductId(productId);
            Set<ColorEntity> colors = new HashSet<>();
            for (Integer colorId : colorIds) {
                Optional<ColorEntity> colorEntity = colorService.findById(colorId);
                colorEntity.ifPresent(colors::add);
            }
            productEntity.get().setColors(colors);
        }
        return productEntity;
    }

    @Override
    public void delete(Integer productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public List<ProductEntity> findAllByCategory(String category) {
        List<ProductEntity> products = StreamSupport.stream(productRepository.findAllByCategory(category).spliterator(), false).collect(Collectors.toList());
        products.forEach(this::setColorsForProduct);
        return products;
    }

    @Override
    public List<ProductEntity> findAllByPriceBetween(Double minPrice, Double maxPrice) {
        List<ProductEntity> products = StreamSupport.stream(productRepository.findAllByPriceBetween(minPrice, maxPrice).spliterator(), false).collect(Collectors.toList());
        products.forEach(this::setColorsForProduct);
        return products;
    }

    @Override
    public List<ProductEntity> priceLessThan(Double price) {
        List<ProductEntity> products = StreamSupport.stream(productRepository.priceLessThan(price).spliterator(), false).collect(Collectors.toList());
        products.forEach(this::setColorsForProduct);
        return products;
    }

    @Override
    public List<ProductEntity> findAllByBrand(String brand) {
        Brand brandEnum = Brand.valueOf(brand);
        List<ProductEntity> products = StreamSupport.stream(productRepository.findAllByBrand(brandEnum).spliterator(), false).collect(Collectors.toList());
        products.forEach(this::setColorsForProduct);
        return products;
    }

    @Override
    public List<ProductEntity> findAllBySize(String size) {
        List<ProductEntity> products = StreamSupport.stream(productRepository.findAllBySize(size).spliterator(), false).collect(Collectors.toList());
        products.forEach(this::setColorsForProduct);
        return products;
    }

    @Override
    public List<ProductEntity> findAllByMaterial(String material) {
        Material materialEnum = Material.valueOf(material);
        List<ProductEntity> products = StreamSupport.stream(productRepository.findAllByMaterial(materialEnum).spliterator(), false).collect(Collectors.toList());
        products.forEach(this::setColorsForProduct);
        return products;
    }

    @Override
    public List<ProductEntity> findAllByProductCondition(String productCondition) {
        ProductCondition productConditionEnum = ProductCondition.valueOf(productCondition);
        List<ProductEntity> products = StreamSupport.stream(productRepository.findAllByProductCondition(productConditionEnum).spliterator(), false).collect(Collectors.toList());
        products.forEach(this::setColorsForProduct);
        return products;
    }

    @Override
    public List<ProductEntity> findAllByAudience(String audience) {
        Audience audienceEnum = Audience.valueOf(audience);
        List<ProductEntity> products = StreamSupport.stream(productRepository.findAllByAudience(audienceEnum).spliterator(), false).collect(Collectors.toList());
        products.forEach(this::setColorsForProduct);
        return products;
    }
}
