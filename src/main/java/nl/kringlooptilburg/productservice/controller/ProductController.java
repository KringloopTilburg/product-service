package nl.kringlooptilburg.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import nl.kringlooptilburg.productservice.domain.dto.ProductDto;
import nl.kringlooptilburg.productservice.domain.dto.ProductImageDto;
import nl.kringlooptilburg.productservice.domain.entities.ProductEntity;
import nl.kringlooptilburg.productservice.mappers.Mapper;
import nl.kringlooptilburg.productservice.services.ProductService;
import nl.kringlooptilburg.productservice.services.rabbit.RabbitMQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/product-service")
public class ProductController {

    private ProductService productService;

    private Mapper<ProductEntity, ProductDto> productMapper;

    private RabbitMQSender rabbitMQSender;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(path = "/products")
    public ResponseEntity<ProductDto> createProduct(@RequestPart String productJson, @RequestPart("images") MultipartFile[] images) {
        ProductDto productDto;

        try {
            productDto = objectMapper.readValue(productJson, ProductDto.class);

            ProductEntity productEntity = productMapper.mapFrom(productDto);
            ProductEntity savedProductEntity = productService.createProduct(productEntity);

            List<ProductImageDto> productImagesDto = new ArrayList<>();
            for (var image : images) {
                var productImageDto = ProductImageDto.builder()
                        .image(image.getBytes())
                        .productId(savedProductEntity.getProductId())
                        .title(image.getOriginalFilename())
                        .build();
                productImagesDto.add(productImageDto);
            }
            rabbitMQSender.sendImages(productImagesDto);

            return new ResponseEntity<>(productMapper.mapTo(savedProductEntity), HttpStatus.CREATED);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(path = "/products")
    public List<ProductDto> listProducts() {
        List<ProductEntity> productEntities = productService.findAll();
        return productEntities.stream()
                .map(productMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/products/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable("productId") Integer productId) {
        Optional<ProductEntity> foundProduct = productService.findOne(productId);
        return foundProduct.map(productEntity -> {
            ProductDto productDto = productMapper.mapTo(productEntity);
            return new ResponseEntity<>(productDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/products/{productId}")
    public ResponseEntity deleteProduct(@PathVariable("productId") Integer productId) {
        productService.delete(productId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/products/category/{category}")
    public List<ProductDto> listProductsByCategory(@PathVariable("category") String category) {
        List<ProductEntity> productEntities = productService.findAllByCategory(category);
        return productEntities.stream()
                .map(productMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/products/price/{minPrice}/{maxPrice}")
    public List<ProductDto> listProductsByPriceBetween(@PathVariable("minPrice") Double minPrice, @PathVariable("maxPrice") Double maxPrice) {
        List<ProductEntity> productEntities = productService.findAllByPriceBetween(minPrice, maxPrice);
        return productEntities.stream()
                .map(productMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/products/price/{price}")
    public List<ProductDto> listProductsByPriceLessThan(@PathVariable("price") Double price) {
        List<ProductEntity> productEntities = productService.priceLessThan(price);
        return productEntities.stream()
                .map(productMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/products/brand/{brand}")
    public List<ProductDto> listProductsByBrand(@PathVariable("brand") String brand) {
        List<ProductEntity> productEntities = productService.findAllByBrand(brand);
        return productEntities.stream()
                .map(productMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/products/size/{size}")
    public List<ProductDto> listProductsBySize(@PathVariable("size") String size) {
        List<ProductEntity> productEntities = productService.findAllBySize(size);
        return productEntities.stream()
                .map(productMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/products/material/{material}")
    public List<ProductDto> listProductsByMaterial(@PathVariable("material") String material) {
        List<ProductEntity> productEntities = productService.findAllByMaterial(material);
        return productEntities.stream()
                .map(productMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/products/product-condition/{productCondition}")
    public List<ProductDto> listProductsByProductCondition(@PathVariable("productCondition") String productCondition) {
        List<ProductEntity> productEntities = productService.findAllByProductCondition(productCondition);
        return productEntities.stream()
                .map(productMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/products/audience/{audience}")
    public List<ProductDto> listProductsByAudience(@PathVariable("audience") String audience) {
        List<ProductEntity> productEntities = productService.findAllByAudience(audience);
        return productEntities.stream()
                .map(productMapper::mapTo)
                .collect(Collectors.toList());
    }
}
