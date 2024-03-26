package nl.kringlooptilburg.productservice.controller;

import nl.kringlooptilburg.productservice.domain.dto.ProductDto;
import nl.kringlooptilburg.productservice.domain.entities.ProductEntity;
import nl.kringlooptilburg.productservice.mappers.Mapper;
import nl.kringlooptilburg.productservice.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product-service")
public class ProductController {

    private ProductService productService;

    private Mapper<ProductEntity, ProductDto> productMapper;

    public ProductController(ProductService productService, Mapper<ProductEntity, ProductDto> productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @PostMapping(path = "/products")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto){
        ProductEntity productEntity = productMapper.mapFrom(productDto);
        ProductEntity savedProductEntity = productService.createProduct(productEntity);
        return new ResponseEntity<>(productMapper.mapTo(savedProductEntity), HttpStatus.CREATED);
    }

    @GetMapping(path = "/products")
    public List<ProductDto> listProducts(){
        List<ProductEntity> productEntities = productService.findAll();
        return productEntities.stream()
                .map(productMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/products/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable("productId") Integer productId){
        Optional<ProductEntity> foundProduct = productService.findOne(productId);
        return foundProduct.map(productEntity -> {
            ProductDto productDto = productMapper.mapTo(productEntity);
            return new ResponseEntity<>(productDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/products/{productId}")
    public ResponseEntity deleteProduct(@PathVariable("productId") Integer productId){
        productService.delete(productId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
