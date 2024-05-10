package back.ciriu.repositories;

import back.ciriu.entities.VerifyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VerifyJpaRepository extends JpaRepository<VerifyEntity, Long> {

    VerifyEntity getByCode(UUID code);
}
