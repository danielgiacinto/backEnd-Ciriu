package back.ciriu.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_detail")
public class OrderDetailEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @JoinColumn(name = "id_order")
    @ManyToOne
    @JsonIgnoreProperties("order")
    private OrderEntity order;

    @JoinColumn(name = "id_product")
    @ManyToOne
    private ProductEntity product;

    @Column
    private Integer quantity;

    @Column
    private BigDecimal price;


}
