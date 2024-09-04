package back.ciriu.models.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiftResponse {

    private String by;

    private String destination;

    private String message;
}
