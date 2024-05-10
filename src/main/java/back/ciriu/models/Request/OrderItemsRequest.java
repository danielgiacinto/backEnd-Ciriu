package back.ciriu.models.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemsRequest {

    private String code;

    private String name;

    private Integer quantity;

    private BigDecimal price;
}
