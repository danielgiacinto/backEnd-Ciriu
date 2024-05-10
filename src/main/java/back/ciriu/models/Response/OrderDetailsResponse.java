package back.ciriu.models.Response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsResponse {


    private ProductResponseDetail product;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal price;
}
