package back.ciriu.repositories;

import back.ciriu.entities.StockDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockDetailJpaRepository extends JpaRepository<StockDetailEntity, Long> {

    List<StockDetailEntity> findByDateBetween(LocalDateTime fromDate, LocalDateTime toDate);

    List<StockDetailEntity> findByDateBetweenAndMovement(LocalDateTime date,LocalDateTime toDate, String movement);

    List<StockDetailEntity> findByMovement(String movement);
}
