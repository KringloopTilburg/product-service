package nl.kringlooptilburg.productservice.repositories;

import nl.kringlooptilburg.productservice.TestDataUtil;
import nl.kringlooptilburg.productservice.config.RabbitMQConfig;
import nl.kringlooptilburg.productservice.domain.entities.ProductEntity;
import nl.kringlooptilburg.productservice.domain.entities.enums.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductRepositoryIntegrationTests {

    private ProductRepository underTest;
    private RabbitMQConfig rabbitMQConfig;

    @Autowired
    public ProductRepositoryIntegrationTests(ProductRepository underTest, RabbitMQConfig rabbitMQConfig) {
        this.underTest = underTest;
        this.rabbitMQConfig = rabbitMQConfig;
    }

    @Transactional
    @Test
    public void testThatProductCanBeCreatedAndRecalled() {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        underTest.save(productEntityA);
        Optional<ProductEntity> result = underTest.findById(productEntityA.getProductId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(productEntityA);
    }

    @Transactional
    @Test
    public void testThatMultipleProductsCanBeCreatedAndRecalled() {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        underTest.save(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        underTest.save(productEntityB);

        Iterable<ProductEntity> result = underTest.findAll();
        assertThat(result)
                .hasSize(2)
                .containsExactly(productEntityA, productEntityB);
    }

    @Transactional
    @Test
    public void testThatProductCanBeDeleted() {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        underTest.save(productEntityA);
        underTest.deleteById(productEntityA.getProductId());
        Optional<ProductEntity> result = underTest.findById(productEntityA.getProductId());
        assertThat(result).isEmpty();
    }

    @Transactional
    @Test
    public void testFindAllByCategory() {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productEntityA.setCategory("Outerwear");
        underTest.save(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        underTest.save(productEntityB);

        Iterable<ProductEntity> result = underTest.findAllByCategory("Outerwear");
        assertThat(result).hasSize(2).containsExactly(productEntityA, productEntityB);
    }

    @Transactional
    @Test
    public void testFindAllByPriceBetween() {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productEntityA.setPrice(100.0);
        underTest.save(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        productEntityB.setPrice(160.0);
        underTest.save(productEntityB);

        Iterable<ProductEntity> result = underTest.findAllByPriceBetween(50.0, 150.0);
        assertThat(result).hasSize(1);
        assertThat(result.iterator().next()).isEqualTo(productEntityA);
    }

    @Transactional
    @Test
    public void testPriceLessThan() {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        productEntityA.setPrice(180.0);
        underTest.save(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        productEntityB.setPrice(160.0);
        underTest.save(productEntityB);

        Iterable<ProductEntity> result = underTest.priceLessThan(170.0);
        assertThat(result).hasSize(1);
        assertThat(result.iterator().next()).isEqualTo(productEntityB);
    }

    @Transactional
    @Test
    public void testFindAllByBrand() {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        underTest.save(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        underTest.save(productEntityB);

        Iterable<ProductEntity> result = underTest.findAllByBrand(Brand.adidas);
        assertThat(result).hasSize(1);
        assertThat(result.iterator().next()).isEqualTo(productEntityA);
    }

    @Transactional
    @Test
    public void testFindAllBySize() {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        underTest.save(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        underTest.save(productEntityB);

        Iterable<ProductEntity> result = underTest.findAllBySize("M");
        assertThat(result).hasSize(1);
        assertThat(result.iterator().next()).isEqualTo(productEntityB);
    }

    @Transactional
    @Test
    public void testFindAllByMaterial() {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        underTest.save(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        underTest.save(productEntityB);

        Iterable<ProductEntity> result = underTest.findAllByMaterial(Material.Cotton);
        assertThat(result).hasSize(1);
        assertThat(result.iterator().next()).isEqualTo(productEntityB);
    }

    @Transactional
    @Test
    public void testFindAllByProductCondition() {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        underTest.save(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        underTest.save(productEntityB);

        Iterable<ProductEntity> result = underTest.findAllByProductCondition(ProductCondition.Good);
        assertThat(result).hasSize(1);
        assertThat(result.iterator().next()).isEqualTo(productEntityA);
    }

    @Transactional
    @Test
    public void testFindAllByAudience() {
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        underTest.save(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        underTest.save(productEntityB);

        Iterable<ProductEntity> result = underTest.findAllByAudience(Audience.Male);
        assertThat(result).hasSize(1);
        assertThat(result.iterator().next()).isEqualTo(productEntityA);
    }
}
