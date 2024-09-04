package back.ciriu.models.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDataRequest {

    private String ShippmentMode;

    private String IdUser;

    private GiftRequest gift;

    private List<OrderItemsRequest> items;

}
