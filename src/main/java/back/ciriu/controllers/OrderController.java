package back.ciriu.controllers;

import back.ciriu.entities.OrderEntity;
import back.ciriu.models.Request.OrderRequest;
import back.ciriu.models.Response.OrderResponse;
import back.ciriu.services.OrderService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("")
    public ResponseEntity<List<OrderResponse>> getAllOrders(@RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDateTime fromDate,
                                                            @RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDateTime toDate,
                                                            @RequestParam(defaultValue = "0") Long status) {
        return ResponseEntity.ok(orderService.getAllOrders(fromDate, toDate, status));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<OrderResponse>> getAllOrderByIdUser(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.getAllOrderByIdUser(id));
    }
    @PostMapping("/new")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid OrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }
}
