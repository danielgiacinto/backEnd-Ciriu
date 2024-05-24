package back.ciriu.services;

import back.ciriu.entities.OrderEntity;
import back.ciriu.models.Request.OrderRequest;
import back.ciriu.models.Response.OrderResponse;
import back.ciriu.models.Response.ReportResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public interface OrderService {

    List<OrderResponse> getAllOrders(LocalDateTime fromDate, LocalDateTime toDate, Long status);

    OrderResponse createOrder(OrderRequest orderRequest);

    List<OrderResponse> getAllOrderByIdUser(UUID id);

    ReportResponse consultReport(LocalDateTime fromDate, LocalDateTime toDate);

    Map<String, List<BigDecimal>> consultReportBar(Integer year);
}
