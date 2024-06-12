package back.ciriu.controllers;

import back.ciriu.entities.StockDetailEntity;
import back.ciriu.models.Request.StockDetailRequest;
import back.ciriu.models.Response.StockDetailResponse;
import back.ciriu.models.Response.SubCategoryResponse;
import back.ciriu.services.StockDetailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/stock")
public class StockDetailController {

    @Autowired
    private StockDetailService stockDetailService;

    @GetMapping("/all")
    public ResponseEntity<Page<StockDetailResponse>> getAllStock(@RequestParam(defaultValue = "0") Integer page,
                                                                 @RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDateTime fromDate,
                                                                 @RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDateTime toDate,
                                                                 @RequestParam(defaultValue = "")String movement) {
        return ResponseEntity.ok(stockDetailService.getAllMovements(page, fromDate, toDate, movement));
    }

    @PostMapping("/newMovement")
    public ResponseEntity<Boolean> newMovement(@RequestBody @Valid StockDetailRequest request) {
        return ResponseEntity.ok(stockDetailService.createMovement(request));
    }
}
