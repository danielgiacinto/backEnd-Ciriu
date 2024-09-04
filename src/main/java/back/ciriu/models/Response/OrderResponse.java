package back.ciriu.models.Response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    @NotNull
    private UUID id;

    @NotNull
    private String status;

    @NotNull
    private String delivery_status;

    @NotNull
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime date;

    @NotNull
    private UserResponse user;

    private GiftResponse gift;

    @NotNull
    private String id_payment;

    @NotNull
    private String shipping;

    @NotNull
    private String format_payment;

    @NotNull
    private String format_method;


    @NotNull
    private List<OrderDetailsResponse> orderDetails;
}
