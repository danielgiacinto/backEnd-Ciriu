package back.ciriu.models.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {

    @NotNull
    private String nameSignUp;

    @NotNull
    private String lastnameSignUp;

    @NotNull @Email
    private String emailSignUp;

    @NotNull
    private String passwordSignUp;

    @NotNull
    private String repeatPasswordSignUp;
}
