package nl.kringlooptilburg.productservice;

import nl.kringlooptilburg.productservice.domain.entities.ColorEntity;
import nl.kringlooptilburg.productservice.domain.entities.ProductEntity;
import nl.kringlooptilburg.productservice.domain.entities.enums.*;

import java.util.HashSet;
import java.util.Set;

public class TestDataUtil {
    private TestDataUtil(){
    }

    public static ProductEntity createTestProductEntityA(){
        Set<ColorEntity> colors = new HashSet<>();
        Set<ProductEntity> emptyProductSet = new HashSet<>();
        colors.add(new ColorEntity(1, Color.BLACK, emptyProductSet));
        colors.add(new ColorEntity(11, Color.GREY, emptyProductSet));

        return ProductEntity.builder()
                .productId(1)
                .name("Grey Ripped Jeans")
                .description("Good condition, size L, grey ripped Jeans.")
                .price(30.0)
                .brand(Brand.ADIDAS)
                .category("Jeans")
                .size("L")
                .material(Material.ACRYLIC)
                .productCondition(ProductCondition.GOOD)
                .colors(colors)
                .audience(Audience.MALE)
                .build();
    }

    public static ProductEntity createTestProductEntityB(){
        Set<ColorEntity> colors = new HashSet<>();
        Set<ProductEntity> emptyProductSet = new HashSet<>();
        colors.add(new ColorEntity(2, Color.WHITE, emptyProductSet));
        colors.add(new ColorEntity(6, Color.YELLOW, emptyProductSet));

        return ProductEntity.builder()
                .productId(2)
                .name("Colourful Jacket")
                .description("New, size M, colourful jacket.")
                .price(20.0)
                .brand(Brand.ZARA)
                .category("Outerwear")
                .size("M")
                .material(Material.COTTON)
                .productCondition(ProductCondition.NEW)
                .colors(colors)
                .audience(Audience.FEMALE)
                .build();
    }

    public static String createExampleProductJson() {
        return "{\n" +
                "  \"productId\": 1,\n" +
                "  \"name\": \"Grey Ripped Jeans\",\n" +
                "  \"description\": \"Good condition, size L, grey ripped Jeans.\",\n" +
                "  \"price\": 30.0,\n" +
                "  \"brand\": \"ADIDAS\",\n" +
                "  \"category\": \"Jeans\",\n" +
                "  \"size\": \"L\",\n" +
                "  \"material\": \"ACRYLIC\",\n" +
                "  \"productCondition\": \"GOOD\",\n" +
                "  \"colors\": [\"RED\", \"BLUE\", \"GREEN\"],\n" +
                "  \"audience\": \"MALE\"\n" +
                "}";
    }

}
