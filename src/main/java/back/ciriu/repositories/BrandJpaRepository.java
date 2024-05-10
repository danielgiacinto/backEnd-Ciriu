package back.ciriu.repositories;

import back.ciriu.entities.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BrandJpaRepository extends JpaRepository<BrandEntity, Long> {
}
