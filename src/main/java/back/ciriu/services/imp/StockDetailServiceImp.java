package back.ciriu.services.imp;

import back.ciriu.entities.OrderEntity;
import back.ciriu.entities.ProductEntity;
import back.ciriu.entities.StockDetailEntity;
import back.ciriu.models.Request.StockDetailRequest;
import back.ciriu.models.Response.StockDetailResponse;
import back.ciriu.repositories.ProductJpaRepository;
import back.ciriu.repositories.StockDetailJpaRepository;
import back.ciriu.services.StockDetailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class StockDetailServiceImp implements StockDetailService {

    @Autowired
    private StockDetailJpaRepository stockDetailJpaRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public Boolean createMovement(StockDetailRequest request) {
        try {
            ProductEntity productEntity = productJpaRepository.getByCodeAndActiveTrue(request.getCode_product());
            if(productEntity == null) {
                throw new RuntimeException("No existe el codigo de producto");
            } else {
                StockDetailEntity stockDetailEntity = new StockDetailEntity();
                stockDetailEntity.setMovement(request.getMovement());
                stockDetailEntity.setDate(request.getDate());
                stockDetailEntity.setQuantity(request.getQuantity());
                stockDetailEntity.setCode_product(request.getCode_product());
                stockDetailJpaRepository.save(stockDetailEntity);
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error en el ingreso o egreso del movimiento, " + e.getMessage());
        }

    }

    @Override
    public Page<StockDetailResponse> getAllMovements(Integer page, LocalDateTime fromDate, LocalDateTime toDate, String movement) {
        List<StockDetailEntity> stockDetailEntities;
        if(fromDate != null && toDate != null && !movement.isEmpty()) {
            stockDetailEntities = stockDetailJpaRepository.findByDateBetweenAndMovement(fromDate, toDate, movement);
        } else if(fromDate != null && toDate != null){
            stockDetailEntities = stockDetailJpaRepository.findByDateBetween(fromDate, toDate);
        } else if (!movement.isEmpty()){
            stockDetailEntities = stockDetailJpaRepository.findByMovement(movement);
        } else {
            stockDetailEntities = stockDetailJpaRepository.findAll();
        }
        // Ordenar los movimientos desde el mas reciente
        Collections.sort(stockDetailEntities, Comparator.comparing(StockDetailEntity::getDate).reversed());

        List<StockDetailResponse> responses = new ArrayList<>();
        for (StockDetailEntity sD : stockDetailEntities){
            responses.add(modelMapper.map(sD, StockDetailResponse.class));
        }
        int pageSize = 10;
        int totalElements = responses.size();
        List<StockDetailResponse> responseList = new ArrayList<>();
        if(!responses.isEmpty()) {
            int totalPages = (int) Math.ceil((double) totalElements / pageSize);
            page = Math.min(Math.max(page, 0), totalPages - 1);
            int fromIndex = page * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, totalElements);
            responseList = responses.subList(fromIndex, toIndex);
        }
        return new PageImpl<>(responseList, PageRequest.of(page, pageSize), totalElements);
    }
}
