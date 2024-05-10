package back.ciriu.repositories;

import back.ciriu.entities.SubCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoryJpaRepository extends JpaRepository<SubCategoryEntity, Long> {
}
