package nl.kringlooptilburg.productservice.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import nl.kringlooptilburg.productservice.domain.entities.enums.Color;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "colors")
public class ColorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "color_id_seq")
    private Integer colorId;

    @Enumerated(EnumType.STRING)
    private Color color;

    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "colors")
    private Set<ProductEntity> products;
}
