package back.ciriu.services;

import back.ciriu.entities.StockDetailEntity;
import back.ciriu.models.Request.StockDetailRequest;
import back.ciriu.models.Response.StockDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface StockDetailService {

    Boolean createMovement(StockDetailRequest request);

    Page<StockDetailResponse> getAllMovements(Integer page, LocalDateTime fromDate, LocalDateTime toDate, String movement);
}
