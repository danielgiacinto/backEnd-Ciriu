package back.ciriu.models.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    @NotNull
    private Long status;

    @NotNull
    private LocalDateTime date;

    @NotNull
    private UUID id_user;

    @NotNull
    private Long id_shipping;

    @NotNull
    private String id_payment;

    @NotNull
    private String format_payment;

    @NotNull
    private String format_method;


    @NotNull
    private List<OrderDetailRequest> orderDetails;
}
