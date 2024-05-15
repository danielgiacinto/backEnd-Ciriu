package back.ciriu.models.Response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductTopResponse {

    @NotNull
    private String code;

    @NotNull
    private Integer quantity;
}
