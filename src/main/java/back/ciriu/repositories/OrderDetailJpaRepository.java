package back.ciriu.repositories;

import back.ciriu.entities.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderDetailJpaRepository extends JpaRepository<OrderDetailEntity, UUID> {
}
