package back.ciriu.models.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailRequest {

    @NotNull
    private String code;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal price;
}
