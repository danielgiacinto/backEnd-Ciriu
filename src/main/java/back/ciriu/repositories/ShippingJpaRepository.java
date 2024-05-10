package back.ciriu.repositories;

import back.ciriu.entities.ShippingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingJpaRepository extends JpaRepository<ShippingEntity, Long> {
}
