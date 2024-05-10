package back.ciriu.models.Response;

import back.ciriu.entities.CategoryEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryResponse {

    @NotNull
    private Long id;

    @NotNull
    private String sub_category;


    @NotNull
    private String category;
}
