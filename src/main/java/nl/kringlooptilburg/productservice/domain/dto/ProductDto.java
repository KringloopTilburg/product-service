package nl.kringlooptilburg.productservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    private Integer productId;

    private String name;

    private String description;

    private Double price;

    private String brand;

    private String category;

    private String size;

    private String material;

    private String productCondition;

    private String color;

    private String audience;

}
