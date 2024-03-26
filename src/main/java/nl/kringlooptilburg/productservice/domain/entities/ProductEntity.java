package nl.kringlooptilburg.productservice.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_seq")
    private Integer productId;

    private String name;

    private String description;

    private Double price;

    private String brand;

    private String category;

    private String size;

    private String material;

    @Column(name = "product_condition")
    private String productCondition;

    private String color;

    private String audience;

}
