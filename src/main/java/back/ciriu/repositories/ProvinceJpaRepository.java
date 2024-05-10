package back.ciriu.repositories;

import back.ciriu.entities.ProvinceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceJpaRepository extends JpaRepository<ProvinceEntity, Long> {
}
