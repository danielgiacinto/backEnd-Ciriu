package back.ciriu.models.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockDetailRequest {

    @NotNull
    private LocalDateTime date;

    @NotNull
    @Positive(message = "La cantidad debe ser mayor a cero")
    private Integer quantity;

    @NotNull
    @Pattern(regexp = "^(Ingreso|Egreso)$", message = "El movimiento debe ser 'Ingreso' o 'Egreso'")
    private String movement;

    @NotNull
    private String code_product;
}
