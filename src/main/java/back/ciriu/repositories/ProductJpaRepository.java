package back.ciriu.repositories;


import back.ciriu.entities.ProductEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {
    ProductEntity getByCodeAndActiveTrue(String code);

    List<ProductEntity> findAllByActiveTrue();

    List<ProductEntity> findAllByActiveTrueAndStockEquals(Integer stock);

    ProductEntity findByCodeEquals(String code);
    
}
