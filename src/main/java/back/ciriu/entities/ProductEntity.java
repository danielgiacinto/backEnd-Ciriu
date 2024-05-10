package back.ciriu.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Array;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column
    private String name;

    @Column
    private String code;

    @JoinColumn(name = "category")
    @ManyToOne
    private CategoryEntity categoryEntity;

    @JoinColumn(name = "sub_category")
    @ManyToOne
    private SubCategoryEntity subCategoryEntity;

    @Column
    private String description;

    @Column
    private BigDecimal price;

    @JoinColumn(name = "brand")
    @ManyToOne
    private BrandEntity brand;

    @Column
    private Integer stock;

    @Column
    private Boolean active;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ImageEntity> images;

}
