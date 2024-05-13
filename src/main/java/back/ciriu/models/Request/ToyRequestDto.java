package back.ciriu.models.Request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToyRequestDto {

    @NotNull
    private String code;

    @NotNull
    private String name;

    @NotNull
    private Long category;

    @NotNull
    private Long subcategory;

    @NotNull
    private String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Long brand;

    @NotNull
    private Integer stock;

    @NotNull
    private List<String> image;

}
