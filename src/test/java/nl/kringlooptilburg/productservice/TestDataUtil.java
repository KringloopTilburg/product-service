package nl.kringlooptilburg.productservice;

import nl.kringlooptilburg.productservice.domain.entities.ProductEntity;
import nl.kringlooptilburg.productservice.domain.entities.enums.*;

public class TestDataUtil {
    private TestDataUtil(){
    }

    public static ProductEntity createTestProductEntityA(){
        return ProductEntity.builder()
                .productId(1)
                .name("Grey Ripped Jeans")
                .description("Good condition, size L, grey ripped jeans.")
                .price(30.0)
                .brand(Brand.adidas)
                .category("Jeans")
                .size("L")
                .material(Material.Acrylic)
                .productCondition(ProductCondition.Good)
                .color(Color.Grey)
                .audience(Audience.Male)
                .build();
    }

    public static ProductEntity createTestProductEntityB(){
        return ProductEntity.builder()
                .productId(2)
                .name("Colourful Jacket")
                .description("New, size M, colourful jacket.")
                .price(20.0)
                .brand(Brand.Zara)
                .category("Outerwear")
                .size("M")
                .material(Material.Cotton)
                .productCondition(ProductCondition.New)
                .color(Color.Multi)
                .audience(Audience.Female)
                .build();
    }

}
