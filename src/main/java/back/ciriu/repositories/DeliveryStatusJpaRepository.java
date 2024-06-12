package back.ciriu.repositories;

import back.ciriu.entities.DeliveryStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryStatusJpaRepository extends JpaRepository<DeliveryStatusEntity, Long>{
}
