package back.ciriu.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "gift")
public class GiftEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String by;

    @Column
    private String destination;

    @Column
    private String message;

    @JoinColumn(name = "id_order")
    @OneToOne
    private OrderEntity order;
}
