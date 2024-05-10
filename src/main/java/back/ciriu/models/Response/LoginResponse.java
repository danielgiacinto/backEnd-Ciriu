package back.ciriu.models.Response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    @NotNull
    private String token;

    @NotNull
    private UUID id;

    @NotNull
    private String rol;
}
