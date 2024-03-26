package nl.kringlooptilburg.productservice.repositories;

import nl.kringlooptilburg.productservice.TestDataUtil;
import nl.kringlooptilburg.productservice.domain.entities.ProductEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductRepositoryIntegrationTests {

    private ProductRepository underTest;

    @Autowired
    public ProductRepositoryIntegrationTests(ProductRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatProductCanBeCreatedAndRecalled(){
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        underTest.save(productEntityA);
        Optional<ProductEntity> result = underTest.findById(productEntityA.getProductId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(productEntityA);
    }

    @Test
    public void testThatMultipleProductsCanBeCreatedAndRecalled(){
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        underTest.save(productEntityA);
        ProductEntity productEntityB = TestDataUtil.createTestProductEntityB();
        underTest.save(productEntityB);

        Iterable<ProductEntity> result = underTest.findAll();
        assertThat(result)
                .hasSize(2)
                .containsExactly(productEntityA, productEntityB);
    }

    @Test
    public void testThatProductCanBeDeleted(){
        ProductEntity productEntityA = TestDataUtil.createTestProductEntityA();
        underTest.save(productEntityA);
        underTest.deleteById(productEntityA.getProductId());
        Optional<ProductEntity> result = underTest.findById(productEntityA.getProductId());
        assertThat(result).isEmpty();
    }

}
