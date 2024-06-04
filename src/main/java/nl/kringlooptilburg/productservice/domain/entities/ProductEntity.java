package nl.kringlooptilburg.productservice.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.kringlooptilburg.productservice.domain.entities.enums.*;

import java.util.Set;

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

    @Enumerated(EnumType.STRING)
    private Brand brand;

    private String category;

    private String size;

    @Enumerated(EnumType.STRING)
    private Material material;

    @Column(name = "product_condition")
    @Enumerated(EnumType.STRING)
    private ProductCondition productCondition;

    @ManyToMany
    @JoinTable(
            name = "product_color",
            joinColumns = @JoinColumn(name = "productId"),
            inverseJoinColumns = @JoinColumn(name = "colorId")
    )
    private Set<ColorEntity> colors;

    @Enumerated(EnumType.STRING)
    private Audience audience;

}
