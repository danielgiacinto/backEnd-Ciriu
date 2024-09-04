package back.ciriu.repositories;

import back.ciriu.entities.GiftEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GiftJpaRepository extends JpaRepository<GiftEntity, Long> {

    GiftEntity getOrderEntityByOrderId(UUID id);
}
