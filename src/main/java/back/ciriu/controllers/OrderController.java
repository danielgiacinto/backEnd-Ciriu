package back.ciriu.controllers;

import back.ciriu.entities.OrderEntity;
import back.ciriu.models.Request.OrderRequest;
import back.ciriu.models.Response.GiftResponse;
import back.ciriu.models.Response.OrderResponse;
import back.ciriu.models.Response.ReportResponse;
import back.ciriu.services.OrderService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("")
    public ResponseEntity<Page<OrderResponse>> getAllOrders(
                                                            @RequestHeader(value = "Authorization") String token,
                                                            @RequestParam(defaultValue = "0") Integer page,
                                                            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDateTime fromDate,
                                                            @RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDateTime toDate,
                                                            @RequestParam(defaultValue = "0") Long status) {
        return ResponseEntity.ok(orderService.getAllOrders(page, fromDate, toDate, status));
    }

    @PostMapping("/new")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Valid OrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @PutMapping("/update/{id}/{id_status}/{id_delivery_status}")
    public ResponseEntity<Boolean> updateOrder(@PathVariable UUID id, @PathVariable Long id_status, @PathVariable Long id_delivery_status){
        return ResponseEntity.ok(orderService.updateOrder(id, id_status, id_delivery_status));
    }

    @GetMapping("/report")
    public ResponseEntity<ReportResponse> consultReports(@RequestParam(value = "fromDate", required = true) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDateTime fromDate,
                                                         @RequestParam(value = "toDate", required = true) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDateTime toDate) {
        return ResponseEntity.ok(orderService.consultReport(fromDate, toDate));
    }

    @GetMapping("/report/month/{year}")
    public ResponseEntity<Map<String, List<BigDecimal>>> consultReportBar(@PathVariable @Valid Integer year) {
        return ResponseEntity.ok(orderService.consultReportBar(year));
    }
}
