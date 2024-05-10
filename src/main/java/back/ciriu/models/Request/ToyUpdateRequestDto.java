package back.ciriu.models.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToyUpdateRequestDto {

    @NotNull
    private String name;

    @NotNull
    private Long category;

    @NotNull
    private Long sub_category;

    @NotNull
    private String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Long brand;

    @NotNull
    private List<String> image;
}
