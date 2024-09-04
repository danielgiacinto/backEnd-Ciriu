package back.ciriu.models.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiftRequest {

    private String by;

    private String destination;

    private String message;
}
