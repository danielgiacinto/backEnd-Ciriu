package back.ciriu.repositories;

import back.ciriu.entities.OrderEntity;
import back.ciriu.entities.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID> {

    List<OrderEntity> getOrderEntityByUserId(UUID id);

    List<OrderEntity> findByDateBetweenAndStatus(LocalDateTime startDate, LocalDateTime endDate, StatusEntity status);

    // Filtrar por fecha entre dos LocalDate
    List<OrderEntity> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Filtrar por estado
    List<OrderEntity> findByStatus(StatusEntity status);

    OrderEntity getOrderEntityById(UUID id);

}
