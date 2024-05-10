package back.ciriu.services;

import back.ciriu.entities.StockDetailEntity;
import back.ciriu.models.Request.StockDetailRequest;
import back.ciriu.models.Response.StockDetailResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface StockDetailService {

    Boolean createMovement(StockDetailRequest request);

    List<StockDetailResponse> getAllMovements(LocalDateTime fromDate, LocalDateTime toDate, String movement);
}
