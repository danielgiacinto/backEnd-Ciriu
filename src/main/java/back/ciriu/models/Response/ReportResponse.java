package back.ciriu.models.Response;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {

    @NotNull
    private BigDecimal sales;

    @NotNull
    private Integer totalOrders;

    private HashMap<String, Integer> methodpayment;

    private ProductTopResponse productTop;
}
