package back.ciriu.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @JoinColumn(name = "id_status")
    @ManyToOne
    private StatusEntity status;

    @JoinColumn(name = "id_delivery_status")
    @ManyToOne
    private DeliveryStatusEntity delivery_status;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime date;

    @JoinColumn(name = "id_user")
    @ManyToOne
    private UserEntity user;

    @JoinColumn(name = "id_shipping")
    @ManyToOne
    private ShippingEntity shipping;

    @Column
    private String id_payment;

    @Column
    private String format_payment;

    @Column
    private String format_method;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    @JsonIgnoreProperties("order")
    private List<OrderDetailEntity> orderDetails;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    @JsonIgnoreProperties("order")
    private GiftEntity gift;
}
