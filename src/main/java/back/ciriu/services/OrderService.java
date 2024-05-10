package back.ciriu.services;

import back.ciriu.entities.OrderEntity;
import back.ciriu.models.Request.OrderRequest;
import back.ciriu.models.Response.OrderResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public interface OrderService {

    List<OrderResponse> getAllOrders(LocalDateTime fromDate, LocalDateTime toDate, Long status);

    OrderResponse createOrder(OrderRequest orderRequest);

    List<OrderResponse> getAllOrderByIdUser(UUID id);
}
