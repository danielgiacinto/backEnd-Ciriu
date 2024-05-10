package back.ciriu.models.Response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {

    private String code;

    private String name;

    @NotNull
    private String category;

    @NotNull
    private String subcategory;

    @NotNull
    private String description;

    @NotNull
    private BigDecimal price;

    private String brand;

    private Integer stock;

    private List<String> image;

    private Boolean active;

}
