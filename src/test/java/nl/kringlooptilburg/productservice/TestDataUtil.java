package nl.kringlooptilburg.productservice;

import nl.kringlooptilburg.productservice.domain.entities.ProductEntity;

public class TestDataUtil {
    private TestDataUtil(){
    }

    public static ProductEntity createTestProductEntityA(){
        return ProductEntity.builder()
                .productId(1)
                .name("Grey Ripped Jeans")
                .description("Good condition, size L, grey ripped jeans.")
                .price(30.0)
                .brand("H&M")
                .category("Jeans")
                .size("L")
                .material("Acrylic")
                .productCondition("Good")
                .color("Grey")
                .audience("Male")
                .build();
    }

    public static ProductEntity createTestProductEntityB(){
        return ProductEntity.builder()
                .productId(2)
                .name("Colourful Jacket")
                .description("New, size M, colourful jacket.")
                .price(20.0)
                .brand("Zara")
                .category("Outerwear")
                .size("M")
                .material("Cotton")
                .productCondition("New")
                .color("Multi")
                .audience("Female")
                .build();
    }

}
