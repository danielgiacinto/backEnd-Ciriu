package back.ciriu.repositories;

import back.ciriu.entities.ImageEntity;
import back.ciriu.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageJpaRepository extends JpaRepository<ImageEntity, Long> {

    List<ImageEntity> getAllByProduct(ProductEntity product);
}
