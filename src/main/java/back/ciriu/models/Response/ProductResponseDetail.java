package back.ciriu.models.Response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDetail {

    private String code;

    private String name;

    @NotNull
    private String category;

    private String brand;

}
