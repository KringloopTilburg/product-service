package nl.kringlooptilburg.productservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.kringlooptilburg.productservice.domain.dto.ProductDto;
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
        colors.add(new ColorEntity(1, Color.Black, emptyProductSet));
        colors.add(new ColorEntity(11, Color.Grey, emptyProductSet));

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
                .colors(colors)
                .audience(Audience.Male)
                .build();
    }

    public static ProductEntity createTestProductEntityB(){
        Set<ColorEntity> colors = new HashSet<>();
        Set<ProductEntity> emptyProductSet = new HashSet<>();
        colors.add(new ColorEntity(2, Color.White, emptyProductSet));
        colors.add(new ColorEntity(6, Color.Yellow, emptyProductSet));

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
                .colors(colors)
                .audience(Audience.Female)
                .build();
    }

    public static String createExampleProductJson() {
        return "{\n" +
                "  \"name\": \"Grey Ripped Jeans\",\n" +
                "  \"description\": \"Good condition, size L, grey ripped jeans.\",\n" +
                "  \"price\": 30.0,\n" +
                "  \"brand\": \"adidas\",\n" +
                "  \"category\": \"Jeans\",\n" +
                "  \"size\": \"L\",\n" +
                "  \"material\": \"Acrylic\",\n" +
                "  \"productCondition\": \"Good\",\n" +
                "  \"colors\": [\"Red\", \"Blue\", \"Green\"],\n" +
                "  \"audience\": \"Male\"\n" +
                "}";
    }

}
